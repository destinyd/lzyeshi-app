

package dd.android.yeshi_trader.core;

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
//        public static final String BOOTSTRAP_ACCOUNT_TYPE = "dd.android.yeshi_trader";
//
//        /**
//         * Account name
//         */
//        public static final String BOOTSTRAP_ACCOUNT_NAME = "FixComputer";
//
//        /**
//         * Provider id
//         */
//        public static final String BOOTSTRAP_PROVIDER_AUTHORITY = "dd.android.yeshi_trader.sync";
//
//        /**
//         * Auth token type
//         */
//        public static final String AUTHTOKEN_TYPE = BOOTSTRAP_ACCOUNT_TYPE;

        public static final String FIXCOMPUTER_ACCOUNT_TYPE = "dd.android.yeshi_trader";

        /**
         * Account name
         */
        public static final String FIXCOMPUTER_ACCOUNT_NAME = "YeshiTrader";

        /**
         * Provider id
         */
        public static final String FIXCOMPUTER_PROVIDER_AUTHORITY = "dd.android.yeshi_trader.sync";

        /**
         * Auth token type
         */
        public static final String AUTHTOKEN_TYPE = FIXCOMPUTER_ACCOUNT_TYPE;
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
        public static final String API_AUTH = URL_BASE + "/oauth/token";

        public static final String API_BASE = "http://api.ysdev.realityandapp.com/v1";

        public static final String API_CHAT_MESSAGES = API_BASE + "/chat_messages.json";

        public static final String FORMAT_API_COMMODITY_CHAT_MESSAGES = API_BASE + "/chat_messages.json?commodity_id=%s";

        public static final String FORMAT_API_GOT_CHAT_MESSAGES = API_BASE + "/got_chat_messages.json?page=%d";

        public static final String FORMAT_API_GOT_CHAT_MESSAGE = API_BASE + "/got_chat_messages/%s.json";
        //
        public static final String API_ME = API_BASE + "/me.json";

        //        public static final String URL_TEST = API_BASE + "/test";
        public static final String API_REG = API_BASE + "/reg";

        public static final String API_LOCATIONS = API_BASE + "/locations.json";

        public static final String API_BUSINESS_HOURS = API_BASE + "/business_hours.json";

        public static final String API_DASHBOARD = API_BASE + "/dashboard.json";

        public static final String FORMAT_API_COMMODITY_BILLS = API_BASE + "/commodities/%s/bills.json";

        public static final String API_GROUPS = API_BASE + "/groups.json";

        public static final String FORMAT_API_GROUP = API_BASE + "/groups/%s.json";

        public static final String FORMAT_API_GROUP_COMMODITIES = API_BASE + "/groups/%s/commodities.json";

        public static final String FORMAT_API_COMMODITY = API_BASE + "/commodities/%s.json";

        public static final String API_COMMODITIES = API_BASE + "/commodities.json";

        public static final String API_BILLS_DASHBOARD = API_BASE + "/bills/dashboard.json";


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
        public static final String INTENT_PREFIX = "dd.android.yeshi_trader.";

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

        public static int NOTIFICATION_ID = 11;

        public static int LAT = 24321838;
        public static int LNG = 109417785;
    }

    public static class Delay {
        private Delay() {
        }

        public static final int SERVICE_SLEEP = 5000;
        public static final int LOCATION_SLEEP = 60000;
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
        public static final String FILE_NAME = "trader_settings.json";
    }

    public static class Picture {
        private Picture() {
        }
        public static final int SCALE_WIDTH = 1024;
    }

    public static class Faye {
        private Faye() {
        }

        public static final String WS_FAYE = "ws://ysdev.realityandapp.com:9292/faye";
    }

}


