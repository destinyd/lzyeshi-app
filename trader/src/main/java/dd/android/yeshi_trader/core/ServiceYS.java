
package dd.android.yeshi_trader.core;

import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static dd.android.yeshi_trader.core.Constants.Http.*;
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

    public static List<Group> getGroups(int page) throws IOException {
        try {
            HttpRequest request = get(API_GROUPS + "?page=" + String.valueOf(page));
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
            String url = String.format(API_LOCATIONS);
            HttpRequest request = get(url);
            String body = request.body();
            List<Location> response = JSON.parseArray(body, Location.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    public static Location postLocation(Location location) throws IOException {
        try {
            String url = String.format(API_LOCATIONS)  + "?" + token();
            HttpRequest request = post(url)
                    .part("location[lat]",location.getLat())
                    .part("location[lng]",location.getLng())
                    ;
            String body = request.body();
            Location response = JSON.parseObject(body, Location.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }


    public static ChatMessage GotChatMessage(String id) throws IOException {
        try {
            String url = String.format(FORMAT_API_GOT_CHAT_MESSAGE,id) + "?" + token();
            HttpRequest request = get(url)
                    .header(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                    .header(HEADER_PARSE_APP_ID, PARSE_APP_ID);
            String body = request.body();
            ChatMessage response = JSON.parseObject(body, ChatMessage.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    public static User getMe() throws IOException {
        try {
            String url = API_ME + "?" + token();
            HttpRequest request = get(url)
                    .header(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                    .header(HEADER_PARSE_APP_ID, PARSE_APP_ID);
            String body = request.body();
            User response = JSON.parseObject(body, User.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    public static List<ChatMessage> GotChatMessages(int page) throws IOException {
        try {
            String url = String.format(FORMAT_API_GOT_CHAT_MESSAGES,page) + "&" + token();
            HttpRequest request = get(url)
                    .header(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                    .header(HEADER_PARSE_APP_ID, PARSE_APP_ID);
            String body = request.body();
            List<ChatMessage> response = JSON.parseArray(body, ChatMessage.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    public static HttpRequest postChatMessage(ChatMessage chatMessage)  throws IOException  {
//        try {
        String url;
        HttpRequest request;
        if(chatMessage.commodity_id != null)
        {
            url = String.format(FORMAT_API_COMMODITY_CHAT_MESSAGES, chatMessage.commodity_id) + "&" + token();
            request = post(url)
                    .part(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                    .part(HEADER_PARSE_APP_ID, PARSE_APP_ID)
//                        .part("chat_message[name]", java.net.URLEncoder.encode(chatMessage.getName(),   "utf-8"))
//                        .part("chat_message[content]", java.net.URLEncoder.encode(chatMessage.getContent(),   "utf-8"))
                    .part("chat_message[name]", chatMessage.getName())
                    .part("chat_message[content]", chatMessage.getContent())
                    .part("chat_message[commodity_id]", chatMessage.getCommodity_id())
            ;
            ;
        }
        else
        {
            url = API_CHAT_MESSAGES + "?" + HEADER_PARSE_ACCESS_TOKEN + "=" + Settings.getFactory().getAuthToken();
            request = post(url)
                    .part(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                    .part(HEADER_PARSE_APP_ID, PARSE_APP_ID)
                    .part("chat_message[name]", chatMessage.getName())
                    .part("chat_message[content]", chatMessage.getContent())
            ;
        }
        return request;
//            String body = request.body();
//            return body;
//            ChatMessage response = JSON.parseObject(body, ChatMessage.class);
//            return response;
//        } catch (HttpRequest.HttpRequestException e) {
//            throw e.getCause();
//        }
    }

    public static OnlineSettings postBusinessHour(Boolean setOpen) throws IOException  {
        String url;
        HttpRequest request;
        if(setOpen)
        {
            url = API_BUSINESS_HOURS + "?" + token();
            request = post(url)
                    .part(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                    .part(HEADER_PARSE_APP_ID, PARSE_APP_ID)
            ;
        }
        else
        {
            url = API_BUSINESS_HOURS + "?" + token();
            request = post(url)
                    .part(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                    .part(HEADER_PARSE_APP_ID, PARSE_APP_ID)
                    .part("business_hour[close]","1")
            ;
        }
        String body = request.body();
        return JSON.parseObject(body,OnlineSettings.class);
    }

    public static OnlineSettings getOnlineSettings() throws IOException  {
        String url;
        HttpRequest request;
            url = API_DASHBOARD + "?" + token();
            request = get(url)
                    .header(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                    .header(HEADER_PARSE_APP_ID, PARSE_APP_ID)
            ;
        String body = request.body();
        return JSON.parseObject(body,OnlineSettings.class);
    }


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

    public static boolean postBill(Bill bill) throws IOException {

        String url;
        HttpRequest request;
        if (bill.commodity_id == null)
            return false;

        url = String.format(FORMAT_API_COMMODITY_BILLS,bill.getCommodity_id()) + "?" + token();
        request = post(url)
                .part(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                .part(HEADER_PARSE_APP_ID, PARSE_APP_ID)
                .part("bill[price]", bill.getPrice())
                .part("bill[total]", bill.getTotal())
                .part("bill[quantity]", bill.getQuantity())
                .part("bill[plus]", bill.getPlus())
        ;
        return request.ok();
    }

    public static Boolean updateGroup(Group group) throws IOException {

        String url;
        HttpRequest request;

        url = String.format(FORMAT_API_GROUP,group.get_id()) + "?" + token();
        request = put(url)
                .part(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                .part(HEADER_PARSE_APP_ID, PARSE_APP_ID)
                .part("group[name]", group.getName())
                .part("group[category_list]", group.getCategory_list())
                .part("group[price]", group.getHumanize_price())
                .part("group[reserve]", group.getReserve())
        ;
        return request.code() == 204;
    }

    public static Boolean postGroup(Group group) throws IOException {

        String url;
        HttpRequest request;

        url = API_GROUPS + "?" + token();
        request = post(url)
                .part(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                .part(HEADER_PARSE_APP_ID, PARSE_APP_ID)
                .part("group[name]", group.getName())
                .part("group[category_list]", group.getCategory_list())
                .part("group[price]", group.getHumanize_price())
                .part("group[reserve]", group.getReserve())
        ;
        return request.code() == 201;
    }

    public static Boolean updateCommodity(Commodity commodity, Commodity p_commodity) throws IOException {

        String url;
        HttpRequest request;

        url = String.format(FORMAT_API_COMMODITY, commodity.get_id()) + "?" + token();
        request = put(url)
                .part(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                .part(HEADER_PARSE_APP_ID, PARSE_APP_ID);
        if (!commodity.getCategory_list().equals(p_commodity.getCategory_list()))
            request.part("commodity[category_list]", p_commodity.getCategory_list());
        if (!commodity.getName().equals(p_commodity.getName()))
            request.part("commodity[name]", p_commodity.getName());
        if (!commodity.getHumanize_price().equals(p_commodity.getHumanize_price()))
            request.part("commodity[price]", p_commodity.getHumanize_price());
        if (commodity.getReserve() != p_commodity.getReserve())
            request.part("commodity[reserve]", p_commodity.getReserve());
        if (!commodity.getDesc().equals(p_commodity.getDesc()))
            request.part("commodity[desc]", p_commodity.getDesc());
        if (p_commodity.getFilePicture() != null)
            request.part("commodity[picture_attributes][image]",p_commodity.getStrPicture(), "multipart/form-data" , p_commodity.getFilePicture());
        ;
        return request.code() == 204;
    }

    public static Boolean postCommodity(Commodity commodity) throws IOException {

        String url;
        HttpRequest request;

        url = String.format(FORMAT_API_GROUP_COMMODITIES,commodity.getGroup_id()) + "?" + token();
        request = post(url)
                .header(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                .header(HEADER_PARSE_APP_ID, PARSE_APP_ID)
                .part("commodity[category_list]", commodity.getCategory_list())
                .part("commodity[name]", commodity.getName())
                .part("commodity[price]", commodity.getHumanize_price())
                .part("commodity[reserve]", commodity.getReserve())
                .part("commodity[desc]", commodity.getDesc())
                .part("commodity[picture_attributes][image]", commodity.getStrPicture(), "multipart/form-data" ,commodity.getFilePicture())
//                .contentType("multipart/form-data")
//                .part("commodity[picture_attributes][image]", new File(commodity.getStrPicture()))
        ;
        return request.code() == 201;
    }

    public static List<Group> getTraderGroups(int page) throws IOException {
        try {
            HttpRequest request = get(API_GROUPS + "?" + token() + "&page=" + String.valueOf(page));
            String body = request.body();
            List<Group> response = JSON.parseArray(body, Group.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    public static List<Commodity> getTraderGroupCommodities(String group_id, int page) throws IOException  {
        try {
            String url = String.format(FORMAT_API_GROUP_COMMODITIES,group_id) + "?page=" + String.valueOf(page) + "&" + token();
            HttpRequest request = get(url);
            String body = request.body();
            List<Commodity> response = JSON.parseArray(body, Commodity.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    public static Commodity apiCommodity(String group_id) throws IOException  {
        try {
            String url = String.format(FORMAT_API_COMMODITY,group_id) + "?" + token();
            HttpRequest request = get(url);
            String body = request.body();
            Commodity response = JSON.parseObject(body, Commodity.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    public static Accounts apiBillDashboard() throws IOException  {
        try {
            String url = API_BILLS_DASHBOARD + "?" + token();
            HttpRequest request = get(url);
            String body = request.body();
            Accounts response = JSON.parseObject(body, Accounts.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    public static Boolean apiDeleteGroup(Group group) throws IOException  {
        try {
            String url;
            HttpRequest request;

            url = String.format(FORMAT_API_GROUP,group.get_id()) + "?" + token();
            request = delete(url)
                    .header(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                    .header(HEADER_PARSE_APP_ID, PARSE_APP_ID)
                    ;
            return request.code() == 204;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    public static Group apiGroup(String id) throws IOException  {
        try {
            String url;
            HttpRequest request;

            url = String.format(FORMAT_API_GROUP,id) + "?" + token();
            request = get(url)
                    .header(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                    .header(HEADER_PARSE_APP_ID, PARSE_APP_ID)
            ;

            String body = request.body();
            Group response = JSON.parseObject(body, Group.class);
            return response;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    public static Boolean deleteCommodity(String commodity_id) throws IOException  {
        try {
            String url;
            HttpRequest request;

            url = String.format(FORMAT_API_COMMODITY,commodity_id) + "?" + token();
            request = delete(url)
                    .header(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY)
                    .header(HEADER_PARSE_APP_ID, PARSE_APP_ID)
            ;
            return request.code() == 204;
        } catch (HttpRequest.HttpRequestException e) {
            throw e.getCause();
        }
    }

    private static String token() {
        return HEADER_PARSE_ACCESS_TOKEN + "=" + Settings.getFactory().getAuthToken();
    }
}
