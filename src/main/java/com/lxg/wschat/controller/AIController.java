/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.controller;

import cn.hutool.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lxg.wschat.dto.AIMessgaeDTO;
import com.lxg.wschat.dto.ChatTOAIDTO;
import com.lxg.wschat.dto.GPTChatDTO;
import com.lxg.wschat.dto.MessageDTO;
import com.lxg.wschat.utils.GetAccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * AIController
 *
 * @author linxugeng
 * @since 2024/1/3
 */
@RestController
@RequestMapping("/ai")
@Slf4j
public class AIController {
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    @Value("${gpt.token}")
    private String gptToken;

    @Value("${gpt2.token}")
    private String gpt2Token;


    @PostMapping("/chat/wxyy/stream")
    public void chatToWxyyStream(HttpServletResponse response,@org.springframework.web.bind.annotation.RequestBody ChatTOAIDTO chatDTO) {
        MediaType mediaType = MediaType.parse("application/json");
        List<AIMessgaeDTO> messageDTOList = chatDTO.getMessageDTOList();
        boolean disableSearch = false;
        boolean enableCitation = false;
        boolean stream = true;
        String userId = chatDTO.getUserId();
        // 创建Gson对象
        Gson gson = new Gson();
        // 创建包含messages的JsonArray
        JsonArray messagesArray = new JsonArray();
        for (AIMessgaeDTO messageDTO : messageDTOList) {
            JsonObject messageObject = new JsonObject();
            messageObject.addProperty("role", messageDTO.getRole());
            messageObject.addProperty("content", messageDTO.getContent());
            messagesArray.add(messageObject);
        }
        // 创建包含disable_search和enable_citation的JsonObject
        JsonObject bodyObject = new JsonObject();
        bodyObject.add("messages", messagesArray);
        bodyObject.addProperty("disable_search", disableSearch);
        bodyObject.addProperty("enable_citation", enableCitation);
        bodyObject.addProperty("stream", stream);
        bodyObject.addProperty("user_id",userId);
        // 将JsonObject转换为字符串
        String requestBodyString = gson.toJson(bodyObject);
        // 创建RequestBody
        RequestBody body = RequestBody.create(mediaType, requestBodyString);
        Request request = null;
        try {
            request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions?access_token="
                            + GetAccessTokenUtil.getToken())
                    .method("POST", body).build();
            // 发送请求并获取响应
            try (Response okHttpResponse = HTTP_CLIENT.newCall(request).execute()) {
                // 设置响应的内容类型和头信息
                response.setContentType("text/event-stream");
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Connection", "keep-alive");
                response.setCharacterEncoding("UTF-8");
                // 获取响应的流
                InputStream inputStream = okHttpResponse.body().byteStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                // 将每个data块作为一块一块的流式数据返回给前端
                PrintWriter writer = response.getWriter();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("data:")) {
                        line = line.substring(6);
                        JSONObject responseJson = new JSONObject(line);
                        if(responseJson.containsKey("result")){
                            System.out.print(responseJson.get("result"));
                            Object o = responseJson.get("result");
                            writer.write(o.toString());
                            writer.flush();
                        }
//                        writer.write(line + "\n\n");
//                        writer.flush();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @PostMapping("/chat/wxyy")
    public String chatToWxyy(@org.springframework.web.bind.annotation.RequestBody ChatTOAIDTO chatDTO) {
        MediaType mediaType = MediaType.parse("application/json");
        List<AIMessgaeDTO> messageDTOList = chatDTO.getMessageDTOList();
        boolean disableSearch = false;
        boolean enableCitation = false;
        String userId = chatDTO.getUserId();
        // 创建Gson对象
        Gson gson = new Gson();
        // 创建包含messages的JsonArray
        JsonArray messagesArray = new JsonArray();
        for (AIMessgaeDTO messageDTO : messageDTOList) {
            JsonObject messageObject = new JsonObject();
            messageObject.addProperty("role", messageDTO.getRole());
            messageObject.addProperty("content", messageDTO.getContent());
            messagesArray.add(messageObject);
        }
        // 创建包含disable_search和enable_citation的JsonObject
        JsonObject bodyObject = new JsonObject();
        bodyObject.add("messages", messagesArray);
        bodyObject.addProperty("disable_search", disableSearch);
        bodyObject.addProperty("enable_citation", enableCitation);
        bodyObject.addProperty("user_id",userId);
        // 将JsonObject转换为字符串
        String requestBodyString = gson.toJson(bodyObject);
        // 创建RequestBody
        RequestBody body = RequestBody.create(mediaType, requestBodyString);
        Request request = null;
        try {
            request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions?access_token="
                            + GetAccessTokenUtil.getToken())
                    .method("POST", body).addHeader("Content-Type", "application/json").build();
            Response response = null;
            response = HTTP_CLIENT.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/chat/gpt/stream")
    public void chatToGptStream(HttpServletResponse response,@org.springframework.web.bind.annotation.RequestBody GPTChatDTO gptChatDTO) {
        MediaType mediaType = MediaType.parse("application/json");
        List<AIMessgaeDTO> messageDTOList = gptChatDTO.getMessageDTOList();
        String model = gptChatDTO.getModel();
        double temperature = 0.5;
        int presencePenalty = 2;
        boolean stream = true;
        // 创建Gson对象
        Gson gson = new Gson();
        // 创建包含messages的JsonArray
        JsonArray messagesArray = new JsonArray();
        for (AIMessgaeDTO messageDTO : messageDTOList) {
            JsonObject messageObject = new JsonObject();
            messageObject.addProperty("role", messageDTO.getRole());
            messageObject.addProperty("content", messageDTO.getContent());
            messagesArray.add(messageObject);
        }
        // 创建包含disable_search和enable_citation的JsonObject
        JsonObject bodyObject = new JsonObject();
        bodyObject.add("messages", messagesArray);
        bodyObject.addProperty("stream", stream);
        bodyObject.addProperty("model", model);
        bodyObject.addProperty("temperature", temperature);
        bodyObject.addProperty("presence_penalty", presencePenalty);
        // 将JsonObject转换为字符串
        String requestBodyString = gson.toJson(bodyObject);
        // 创建RequestBody
        RequestBody body = RequestBody.create(mediaType, requestBodyString);
        Request request = null;
        try {
            request = new Request.Builder()
                    .url("https://ngedlktfticp.cloud.sealos.io/v1/chat/completions")
                    .method("POST", body)
                    .addHeader("Authorization",gptToken).build();
//            Response response = null;
//            response = HTTP_CLIENT.newCall(request).execute();
//            return response.body().string();

//            // 发送请求并获取响应
            try (Response okHttpResponse = HTTP_CLIENT.newCall(request).execute()) {
                // 设置响应的内容类型和头信息
                response.setContentType("text/event-stream");
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Connection", "keep-alive");
                response.setCharacterEncoding("UTF-8");
                // 获取响应的流
                InputStream inputStream = okHttpResponse.body().byteStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                // 将每个data块作为一块一块的流式数据返回给前端
                PrintWriter writer = response.getWriter();
                String line;
                while ((line = reader.readLine()) != null) {
                    if(line.equals("data: [DONE]")){
                        System.out.println("\n[DONE]");
//                        writer.write("\n[DONE]");
//                        writer.flush();
                    }else if (line.startsWith("data:")) {
                        line = line.substring(6);
                        JSONObject responseJson = new JSONObject(line);
                        if(responseJson.getJSONArray("choices").getJSONObject(0).getJSONObject("delta").containsKey("content")){
                            System.out.print(responseJson.getJSONArray("choices").getJSONObject(0).getJSONObject("delta").get("content"));
                            Object o = responseJson.getJSONArray("choices").getJSONObject(0).getJSONObject("delta").get("content");
                            writer.write(o.toString());
                            writer.flush();
                        }
//                        writer.write(line + "\n\n");
//                        writer.flush();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @PostMapping("/chat/gpt")
    public String chatToGpt(@org.springframework.web.bind.annotation.RequestBody GPTChatDTO gptChatDTO) {
        log.info("gptChatDTO:{}",gptChatDTO);
        MediaType mediaType = MediaType.parse("application/json");
        List<AIMessgaeDTO> messageDTOList = gptChatDTO.getMessageDTOList();
        String model = gptChatDTO.getModel();
        double temperature = 0.5;
        int presencePenalty = 2;
        boolean stream = false;
        // 创建Gson对象
        Gson gson = new Gson();
        // 创建包含messages的JsonArray
        JsonArray messagesArray = new JsonArray();
        for (AIMessgaeDTO messageDTO : messageDTOList) {
            JsonObject messageObject = new JsonObject();
            messageObject.addProperty("role", messageDTO.getRole());
            messageObject.addProperty("content", messageDTO.getContent());
            messagesArray.add(messageObject);
        }
        // 创建包含disable_search和enable_citation的JsonObject
        JsonObject bodyObject = new JsonObject();
        bodyObject.add("messages", messagesArray);
        bodyObject.addProperty("stream", stream);
        bodyObject.addProperty("model", model);
        bodyObject.addProperty("temperature", temperature);
        bodyObject.addProperty("presence_penalty", presencePenalty);
        // 将JsonObject转换为字符串
        String requestBodyString = gson.toJson(bodyObject);
        // 创建RequestBody
        RequestBody body = RequestBody.create(mediaType, requestBodyString);
        Request request = null;
        try {
            request = new Request.Builder()
                    .url("https://ngedlktfticp.cloud.sealos.io/v1/chat/completions")
                    .method("POST", body).addHeader("Content-Type", "application/json")
                    .addHeader("Authorization",gptToken).build();
            Response response = null;
            response = HTTP_CLIENT.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    //gpt-3.5-turbo, embedding, gpt-4
    @PostMapping("/chat/gpt2")
    public String chatToGpt2(@org.springframework.web.bind.annotation.RequestBody GPTChatDTO gptChatDTO) {
        MediaType mediaType = MediaType.parse("application/json");
        List<AIMessgaeDTO> messageDTOList = gptChatDTO.getMessageDTOList();
        String model = gptChatDTO.getModel();
        double temperature = 0.5;
        int presencePenalty = 2;
        boolean stream = false;
        // 创建Gson对象
        Gson gson = new Gson();
        // 创建包含messages的JsonArray
        JsonArray messagesArray = new JsonArray();
        for (AIMessgaeDTO messageDTO : messageDTOList) {
            JsonObject messageObject = new JsonObject();
            messageObject.addProperty("role", messageDTO.getRole());
            messageObject.addProperty("content", messageDTO.getContent());
            messagesArray.add(messageObject);
        }
        // 创建包含disable_search和enable_citation的JsonObject
        JsonObject bodyObject = new JsonObject();
        bodyObject.add("messages", messagesArray);
        bodyObject.addProperty("stream", stream);
        bodyObject.addProperty("model", model);
        bodyObject.addProperty("temperature", temperature);
        bodyObject.addProperty("presence_penalty", presencePenalty);
        // 将JsonObject转换为字符串
        String requestBodyString = gson.toJson(bodyObject);
        // 创建RequestBody
        RequestBody body = RequestBody.create(mediaType, requestBodyString);
        Request request = null;
        try {
            request = new Request.Builder()
                    .url("https://api.chatanywhere.tech/v1/chat/completions")
                    .method("POST", body).addHeader("Content-Type", "application/json")
                    .addHeader("Authorization",gpt2Token).build();
            Response response = null;
            response = HTTP_CLIENT.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/chat/gpt2/stream")
    public void chatToGpt2Stream(HttpServletResponse response,@org.springframework.web.bind.annotation.RequestBody GPTChatDTO gptChatDTO) {
        MediaType mediaType = MediaType.parse("application/json");
        List<AIMessgaeDTO> messageDTOList = gptChatDTO.getMessageDTOList();
        String model = gptChatDTO.getModel();
        double temperature = 0.5;
        int presencePenalty = 2;
        boolean stream = true;
        // 创建Gson对象
        Gson gson = new Gson();
        // 创建包含messages的JsonArray
        JsonArray messagesArray = new JsonArray();
        for (AIMessgaeDTO messageDTO : messageDTOList) {
            JsonObject messageObject = new JsonObject();
            messageObject.addProperty("role", messageDTO.getRole());
            messageObject.addProperty("content", messageDTO.getContent());
            messagesArray.add(messageObject);
        }
        // 创建包含disable_search和enable_citation的JsonObject
        JsonObject bodyObject = new JsonObject();
        bodyObject.add("messages", messagesArray);
        bodyObject.addProperty("stream", stream);
        bodyObject.addProperty("model", model);
        bodyObject.addProperty("temperature", temperature);
        bodyObject.addProperty("presence_penalty", presencePenalty);
        // 将JsonObject转换为字符串
        String requestBodyString = gson.toJson(bodyObject);
        // 创建RequestBody
        RequestBody body = RequestBody.create(mediaType, requestBodyString);
        Request request = null;
        try {
            request = new Request.Builder()
                    .url("https://api.chatanywhere.tech/v1/chat/completions")
                    .method("POST", body).addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", gpt2Token).build();
            // 发送请求并获取响应
            try (Response okHttpResponse = HTTP_CLIENT.newCall(request).execute()) {
                // 设置响应的内容类型和头信息
                response.setContentType("text/event-stream");
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Connection", "keep-alive");
                response.setCharacterEncoding("UTF-8");
                // 获取响应的流
                InputStream inputStream = okHttpResponse.body().byteStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                // 将每个data块作为一块一块的流式数据返回给前端
                PrintWriter writer = response.getWriter();
                String line;
                while ((line = reader.readLine()) != null) {
                    if(line.equals("data: [DONE]")){
                        System.out.println("\n[DONE]");
//                        writer.write("\n[DONE]");
//                        writer.flush();
                    }else if (line.startsWith("data:")) {
                        line = line.substring(6);
                        JSONObject responseJson = new JSONObject(line);
                        if(responseJson.getJSONArray("choices").getJSONObject(0).getJSONObject("delta").containsKey("content")){
                            System.out.print(responseJson.getJSONArray("choices").getJSONObject(0).getJSONObject("delta").get("content"));
                            Object o = responseJson.getJSONArray("choices").getJSONObject(0).getJSONObject("delta").get("content");
                            writer.write(o.toString());
                            writer.flush();
                        }
//                        writer.write(line + "\n\n");
//                        writer.flush();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/draw/gpt")
    public void drawToGptStream(HttpServletResponse response,String prompt) {
        MediaType mediaType = MediaType.parse("application/json");
        AIMessgaeDTO messageDTO1 = new AIMessgaeDTO();
        messageDTO1.setRole("user");
        messageDTO1.setContent(prompt);
        List<AIMessgaeDTO> messageDTOList = Arrays.asList(messageDTO1);
        String model = "midjourney";
        double temperature = 0.5;
        int presencePenalty = 2;
        boolean stream = true;
        // 创建Gson对象
        Gson gson = new Gson();
        // 创建包含messages的JsonArray
        JsonArray messagesArray = new JsonArray();
        for (AIMessgaeDTO messageDTO : messageDTOList) {
            JsonObject messageObject = new JsonObject();
            messageObject.addProperty("role", messageDTO.getRole());
            messageObject.addProperty("content", messageDTO.getContent());
            messagesArray.add(messageObject);
        }
        // 创建包含disable_search和enable_citation的JsonObject
        JsonObject bodyObject = new JsonObject();
        bodyObject.add("messages", messagesArray);
        bodyObject.addProperty("stream", stream);
        bodyObject.addProperty("model", model);
        bodyObject.addProperty("temperature", temperature);
        bodyObject.addProperty("presence_penalty", presencePenalty);
        // 将JsonObject转换为字符串
        String requestBodyString = gson.toJson(bodyObject);
        // 创建RequestBody
        RequestBody body = RequestBody.create(mediaType, requestBodyString);
        Request request = null;
        try {
            request = new Request.Builder()
                    .url("https://ngedlktfticp.cloud.sealos.io/v1/chat/completions")
                    .method("POST", body)
                    .addHeader("Authorization",gptToken).build();
//            Response response = null;
//            response = HTTP_CLIENT.newCall(request).execute();
//            return response.body().string();

//            // 发送请求并获取响应
            try (Response okHttpResponse = HTTP_CLIENT.newCall(request).execute()) {
                // 设置响应的内容类型和头信息
                response.setContentType("text/event-stream");
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Connection", "keep-alive");
                response.setCharacterEncoding("UTF-8");
                // 获取响应的流
                InputStream inputStream = okHttpResponse.body().byteStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                // 将每个data块作为一块一块的流式数据返回给前端
                PrintWriter writer = response.getWriter();
                String line;
                while ((line = reader.readLine()) != null) {
                    if(line.equals("data: [DONE]")){
                        System.out.println("\n[DONE]");
//                        writer.write("\n[DONE]");
//                        writer.flush();
                    }else if (line.startsWith("data:")) {
                        line = line.substring(6);
                        JSONObject responseJson = new JSONObject(line);
                        if(responseJson.getJSONArray("choices").getJSONObject(0).getJSONObject("delta").containsKey("content")){
                            System.out.print(responseJson.getJSONArray("choices").getJSONObject(0).getJSONObject("delta").get("content"));
                            Object o = responseJson.getJSONArray("choices").getJSONObject(0).getJSONObject("delta").get("content");
                            writer.write(o.toString());
                            writer.flush();
                        }
//                        writer.write(line + "\n\n");
//                        writer.flush();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
