

package dd.android.yeshi.core;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Bootstrap constants
 */
public class Constants {

    public static class Auth {
        private Auth() {
        }

//        /**
//         * Account type id
//         */
//        public static final String BOOTSTRAP_ACCOUNT_TYPE = "dd.android.yeshi";
//
//        /**
//         * Account name
//         */
//        public static final String BOOTSTRAP_ACCOUNT_NAME = "FixComputer";
//
//        /**
//         * Provider id
//         */
//        public static final String BOOTSTRAP_PROVIDER_AUTHORITY = "dd.android.yeshi.sync";
//
//        /**
//         * Auth token type
//         */
//        public static final String AUTHTOKEN_TYPE = BOOTSTRAP_ACCOUNT_TYPE;

        public static final String YESHI_ACCOUNT_TYPE = "dd.android.yeshi";

        /**
         * Account name
         */
        public static final String YESHI_ACCOUNT_NAME = "Yeshi";

        /**
         * Provider id
         */
        public static final String YESHI_PROVIDER_AUTHORITY = "dd.android.yeshi.sync";

        /**
         * Auth token type
         */
        public static final String AUTHTOKEN_TYPE = YESHI_ACCOUNT_TYPE;
    }

    /**
     * All HTTP is done through a REST style API built for demonstration purposes on Parse.com
     * Thanks to the nice people at Parse for creating such a nice system for us to use for fixcomputer!
     */
    public static class Http {
        private Http() {
        }


        public static final String URL_BASE = "http://ysdev.realityandapp.com";

//        public static final String URL_ADMIN_BASE = URL_BASE + "/xiaofengxiaomi";

        public static final String URL_GROUPS = URL_BASE + "/groups.json";

        public static final String FORMAT_URL_GROUP = URL_BASE + "/groups/%s.json";

        public static final String FORMAT_URL_TRADER = URL_BASE + "/traders/%s.json";

        public static final String URL_LOCATIONS = URL_BASE + "/locations.json";

        public static final String FORMAT_URL_TRADER_LOCATIONS = URL_BASE + "/traders/%s/locations.json";

        public static final String FORMAT_URL_COMMODITIES = URL_BASE + "/commodities.json?page=%d";

        public static final String FORMAT_URL_GROUP_COMMODITIES = URL_BASE + "/groups/%s/commodities.json?page=%d";

//        public static final String FORMAT_URL_PROBLEM = URL_BASE + "/android/%s.json";
//
//        public static final String FORMAT_URL_PROBLEM_WITH_UUID = URL_BASE + "/android/%s.json?uuid=%s";
//
//        public static final String FORMAT_URL_PROBLEM_WITH_UUID_STATUS = URL_BASE + "/android/%s.json?uuid=%s&problem[status]=%s&proble[status_plus]=%s";
//
//        public static final String FORMAT_URL_STATUS = URL_BASE + "/status.json?uuid=%s";
//
//        public static final String URL_PRICING = URL_BASE + "/pricing.json";
//        public static final String URL_BASE = "http://fixcomputer.com";
        /**
         * Authentication URL
         */
        public static final String URL_AUTH = URL_BASE + "/oauth/token";

        public static final String API_BASE = "http://api.ysdev.realityandapp.com/v1";

        public static final String CHAT_MESSAGES = API_BASE + "/chat_messages.json";

        public static final String FORMAT_URL_COMMODITY_CHAT_MESSAGES = API_BASE + "/chat_messages.json?commodity_id=%s";

        public static final String FORMAT_URL_GOT_CHAT_MESSAGES = API_BASE + "/got_chat_messages.json?page=%d";

        public static final String FORMAT_URL_GOT_CHAT_MESSAGE = API_BASE + "/got_chat_messages/%s.json";
        //
        public static final String URL_ME = API_BASE + "/me.json";

//        public static final String URL_TEST = API_BASE + "/test";
//        public static final String URL_REG = API_BASE + "/reg";
//
//        /**
//         * List Users URL
//         */
//        public static final String URL_USERS = API_BASE + "/users.json";
//
//        public static final String FORMAT_URL_USERS = API_BASE + "/users.json?page=%d&";
//
//        public static final String FORMAT_URL_USER = API_BASE + "/users/%s.json";
//        /**
//         * List Friend URL
//         */
//        public static final String URL_FRIEND = API_BASE + "/users/friend.json";
//        public static final String FORMAT_URL_FRIEND = API_BASE + "/users/friend.json?page=%d&";
//
//        public static final String FORMAT_URL_USER_FOLLOW = API_BASE + "/users/%s/follow.json";
//
//        /**
//         * List Checkin's URL
//         */
//        public static final String URL_ACTIVITIES = API_BASE + "/activities.json";
//
//        public static final String FORMAT_URL_ACTIVITIES = API_BASE + "/activities.json?page=%d&";
//
//        public static final String URL_NOTIFICATIONS = API_BASE + "/notifications.json";
//
//        public static final String URL_NOTIFICATIONS_STATUS = API_BASE + "/notifications/status.json";
//
//        public static final String FORMAT_URL_NOTIFICATION = API_BASE + "/notifications/%s.json";
//
//        public static final String FORMAT_URL_REPLY_NOTIFICATION = API_BASE + "/notifications/%s/%s.json";
//
//        public static final String FORMAT_URL_ACTIVITY = API_BASE + "/activities/%s.json";
//
//        public static final String FORMAT_URL_ACTIVITY_REQUEST = API_BASE + "/activities/%s/activity_requests.json";
//        public static final String FORMAT_URL_ACTIVITY_QUIT = API_BASE + "/activities/%s/quit.json";
//        public static final String FORMAT_URL_ACTIVITY_CLOSE = API_BASE + "/activities/%s/close.json";
//        public static final String FORMAT_URL_ACTIVITY_INVITE_FRIEND = API_BASE + "/activities/%s/invite.json";
//        public static final String FORMAT_ACCESS_TOKEN = "%s=%s";
//
        public static final String PARSE_APP_ID = "4957a682a1b180485bcff93db383848f425880738fb68eed755b18cc8a100bde";
        public static final String PARSE_REST_API_KEY = "6e57151ad4f0dbc8df1d0864f3710e18e37404cf2c1a02f9fc488a5cab78c721";
        public static final String HEADER_PARSE_REST_API_KEY = "client_secret";
        public static final String HEADER_PARSE_APP_ID = "client_id";
        public static final String CONTENT_TYPE_JSON = "application/json";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String SESSION_TOKEN = "sessionToken";
//
        public static final String HEADER_PARSE_GRANT_TYPE = "grant_type";
        public static final String GRANT_TYPE = "password";
        public static final String HEADER_PARSE_ACCESS_TOKEN = "access_token";


        /**
         * Base URL for all requests
         */
//        public static final String URL_BASE = "https://api.parse.com";

        /**
         * Authentication URL
         */
//        public static final String URL_AUTH = URL_BASE + "/1/login";

        /**
         * List Users URL
         */
//        public static final String URL_USERS = URL_BASE + "/1/users";
//
//        /**
//         * List News URL
//         */
//        public static final String URL_NEWS = URL_BASE + "/1/classes/News";
//
//        /**
//         * List Checkin's URL
//         */
//        public static final String URL_CHECKINS = URL_BASE + "/1/classes/Locations";
    }


    public static class Extra {
        private Extra() {
        }
        public static final String PROBLEM = "Problem";
        public static final String GROUP = "Group";
        public static final String APIKEY = "APIKEY";
        public static final String COMMODITY = "Commodity";
        public static final String TRADER = "Trader";
        public static final String CHAT_MESSAGE = "ChatMessage";

        public static final String NAME = "Name";
    }

    public static class Intent {
        private Intent() {
        }

        /**
         * Action prefix for all intents created
         */
        public static final String INTENT_PREFIX = "dd.android.yeshi.";

    }

    public static class Other {
        private Other() {
        }

        public static final SimpleDateFormat POST_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        public static final SimpleDateFormat TODAY_DATE_FORMAT = new SimpleDateFormat("HH:mm");
        public static final SimpleDateFormat THIS_MONTH_DATE_FORMAT = new SimpleDateFormat("M月d日");
        public static final SimpleDateFormat OTHER_DATE_FORMAT = new SimpleDateFormat("yy-MM-dd");

        public static final TimeZone CHINA_TIME_ZONE = TimeZone.getTimeZone("Asia/Shanghai");
        public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
        public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getDefault();

        public static class ActivityTaskStatus {
            private ActivityTaskStatus() {
            }

            public static final int JOIN = 0;
            public static final int QUIT = 1;
            public static final int INVITE = 2;
            public static final int CLOSE = 3;
        }

    }

    public static class Delay {
        private Delay() {
        }

        public static final int GET_STATUS = 300000;
//        public static final int GET_STATUS = 600000;
    }

    public static class ToolBar {
        private ToolBar() {
        }

        public static final int PRICING = 0;
        public static final int PROBLEM_FORM = 1;
        public static final int PROBLEMS = 2;
//        public static final int MENU = 3;
//        public static final int COUNT = 4;
        public static final int COUNT = 3;
    }

    public static class Setting {
        private Setting() {
        }
        public static final String SDCARD_PATH = "/.yeshi";
        public static final String FILE_NAME = "settings.json";
    }

    public static class Faye {
        private Faye() {
        }

        public static final String WS_FAYE = "ws://ysdev.realityandapp.com:9292/faye";
    }

}


