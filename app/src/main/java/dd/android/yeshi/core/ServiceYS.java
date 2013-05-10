
package dd.android.yeshi.core;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;
import roboguice.util.Strings;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static dd.android.yeshi.core.Constants.Http.*;
import static com.github.kevinsawicki.http.HttpRequest.*;


public class ServiceYS {

    /**
     * Read and connect timeout in milliseconds
     */
    private static final int TIMEOUT = 30 * 1000;


//    public static Problem sendProblem(Problem problem, String uuid) {
//        Problem result_problem = null;
//        HttpRequest request = post(URL_PROBLEMS)
//                .part("problem[phone]", problem.getPhone())
//                .part("problem[name]", problem.getName())
//                .part("problem[address]", problem.getAddress())
//                .part("problem[address_plus]", problem.getAddress_plus())
//                .part("problem[phone]", problem.getPhone())
//                .part("problem[lat]", problem.getLat())
//                .part("problem[lng]", problem.getLng())
//                .part("problem[desc]", problem.getDesc())
//                .part("problem[uuid]", uuid);
//
//
//        Log.d("Auth", "response=" + request.code());
//
//        if (request.ok()) {
//            String tmp = Strings.toString(request.buffer());
//            Log.d("response body:", tmp);
//            result_problem = JSON.parseObject(tmp, Problem.class);
//        }
//        return result_problem;
//    }

    public static Trader getTrader(String id) throws IOException {
        try {
            HttpRequest request = get(String.format(FORMAT_URL_TRADER,id) );
            String body = request.body();
            Trader response = JSON.parseObject(body, Trader.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    public static Group getGroup(String id) throws IOException {
        try {
            HttpRequest request = get(String.format(FORMAT_URL_GROUP,id) );
            String body = request.body();
            Group response = JSON.parseObject(body, Group.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    public static List<Group> getGroups(int page) throws IOException {
        try {
            HttpRequest request = get(URL_GROUPS + "?page=" + String.valueOf(page));
            String body = request.body();
            List<Group> response = JSON.parseArray(body, Group.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    public static List<Commodity> getGroupCommodities(String group_id, int page) throws IOException  {
        try {
            String url = String.format(FORMAT_URL_GROUP_COMMODITIES,group_id,page);
            HttpRequest request = get(url);
            String body = request.body();
            List<Commodity> response = JSON.parseArray(body, Commodity.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    public static List<Commodity> getCommodities(int page) throws IOException {
        try {
            String url = String.format(FORMAT_URL_COMMODITIES,page);
            HttpRequest request = get(url);
            String body = request.body();
            List<Commodity> response = JSON.parseArray(body, Commodity.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    public static List<Location> getLocations() throws IOException  {
        try {
            String url = String.format(URL_LOCATIONS);
            HttpRequest request = get(url);
            String body = request.body();
            List<Location> response = JSON.parseArray(body, Location.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    public static List<Location> getTraderLocations(String trader_id) throws IOException  {
        try {
            String url = String.format(FORMAT_URL_TRADER_LOCATIONS,trader_id);
            HttpRequest request = get(url);
            String body = request.body();
            List<Location> response = JSON.parseArray(body, Location.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    public static ChatMessage postChatMessage(ChatMessage chatMessage)  throws IOException  {
        try {
            String url;
            HttpRequest request;
            if(chatMessage.commodity_id != null)
            {
                url = String.format(FORMAT_URL_COMMODITY_CHAT_MESSAGES,chatMessage.commodity_id);
                request = post(url)
                        .part("chat_message[name]", chatMessage.getName())
                        .part("chat_message[content]", chatMessage.getContent())
                        .part("chat_message[commodity_id]", chatMessage.getCommodity_id())
                ;
            }
            else
            {
                url = CHAT_MESSAGES;
                request = post(url)
                        .part("chat_message[name]", chatMessage.getName())
                        .part("chat_message[content]", chatMessage.getContent());
            }
            String body = request.body();
            ChatMessage response = JSON.parseObject(body, ChatMessage.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

//    public static List<Price> getPrices(String uuid) throws IOException {
//        try {
//            HttpRequest request = get(URL_PRICING + "?" + "uuid=" + uuid);
//            String body = request.body();
//            List<Price> response = JSON.parseArray(body, Price.class);
//            return response;
//        } catch (HttpRequest.HttpRequestException e) {
//            throw e.getCause();
//        } catch (Exception e) {
//            return Collections.emptyList();
//        }
//    }
//
//    public static Problem getProblem(String problem_id, String uuid) throws IOException {
//        try {
//            String url = String.format(FORMAT_URL_PROBLEM_WITH_UUID, problem_id, uuid);
//            HttpRequest request = get(url);
//            String body = request.body();
//            Problem respons = JSON.parseObject(body, Problem.class);
//            return respons;
//        } catch (HttpRequest.HttpRequestException e) {
//            throw e.getCause();
//        }
//    }
//
//    public static Problem updateProblem(String problem_id, String uuid, String status,String plus) throws IOException {
//        try {
//            String url = String.format(FORMAT_URL_PROBLEM_WITH_UUID_STATUS, problem_id, uuid, status, plus);
//            HttpRequest request = post(url)
//                    ;
//            String body = request.body();
//            if(request.ok())
//            {
//                Problem respons = JSON.parseObject(body, Problem.class);
//                return respons;
//            }
//            return null;
//        } catch (HttpRequest.HttpRequestException e) {
//            throw e.getCause();
//        }
//    }
//
//    public static Problem updateProblemForAdmin(String problem_id, String uuid, String status,String plus) throws IOException {
//        try {
//            String url = String.format(ADMIN_FORMAT_URL_PROBLEM_UPDATE, problem_id, uuid, status, plus);
//            HttpRequest request = put(url)
//                    ;
//            String body = request.body();
//            if(request.ok())
//            {
//                Problem respons = JSON.parseObject(body, Problem.class);
//                return respons;
//            }
//            return null;
//        } catch (HttpRequest.HttpRequestException e) {
//            throw e.getCause();
//        }
//    }
//
//    public static ProblemStatus getStatus(String uuid) throws IOException {
//        try {
//            String url = String.format(FORMAT_URL_STATUS, uuid);
//            HttpRequest request = get(url)
//                    ;
//            String body = request.body();
//            if(request.ok())
//            {
//                ProblemStatus respons = JSON.parseObject(body, ProblemStatus.class);
//                return respons;
//            }
//            return null;
//        } catch (HttpRequest.HttpRequestException e) {
//            throw e.getCause();
//        }
//    }
//
//    public static ProblemStatus getStatusForAdmin() throws IOException {
//        try {
//            String url = String.format(ADMIN_URL_STATUS);
//            HttpRequest request = get(url)
//                    ;
//            String body = request.body();
//            if(request.ok())
//            {
//                ProblemStatus respons = JSON.parseObject(body, ProblemStatus.class);
//                return respons;
//            }
//            return null;
//        } catch (HttpRequest.HttpRequestException e) {
//            throw e.getCause();
//        }
//    }
//
//    public static List<Problem> getProblemsForAdmin(int page) throws IOException {
//        try {
//            HttpRequest request = get(ADMIN_URL_PROBLEMS + "?page=" + String.valueOf(page));
//            String body = request.body();
//            List<Problem> response = JSON.parseArray(body, Problem.class);
//            return response;
//        } catch (HttpRequest.HttpRequestException e) {
//            throw e.getCause();
//        } catch (Exception e) {
//            return Collections.emptyList();
//        }
//    }
//
//    public static Problem getProblemForAdmin(String problem_id) throws IOException {
//        try {
//            String url = String.format(ADMIN_FORMAT_URL_PROBLEM, problem_id);
//            HttpRequest request = get(url);
//            String body = request.body();
//            Problem respons = JSON.parseObject(body, Problem.class);
//            return respons;
//        } catch (HttpRequest.HttpRequestException e) {
//            throw e.getCause();
//        }
//    }
}
