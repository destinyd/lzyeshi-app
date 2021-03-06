package dd.android.yeshi.core;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: dd
 * Date: 13-1-10
 * Time: 下午12:53
 * To change this template use File | Settings | File Templates.
 */
public class Picture implements Serializable {

    private static final long serialVersionUID = -7545300822736333813L;

    public String _id;
    Image image;

    public class Image implements Serializable {
        private static final long serialVersionUID = -4455210258042955560L;

        public class Thumb implements Serializable {
            private static final long serialVersionUID = -2085974644325139332L;
            public String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public class Android implements Serializable {
            private static final long serialVersionUID = -2085974644325134232L;
            public String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        String url;
        Thumb thumb;
        Android android;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Thumb getThumb() {
            return thumb;
        }

        public void setThumb(Thumb thumb) {
            this.thumb = thumb;
        }

        public Android getAndroid() {
            return android;
        }

        public void setAndroid(Android android) {
            this.android = android;
        }
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getUrl(){
        if(image == null)
            return "";
        else
            return image.getUrl();
    }

    public String getThumbUrl(){
        if(image == null)
            return "";
        else
            return image.getThumb().getUrl();
    }

    public String getAndroidUrl(){
        if(image == null)
            return "";
        else
            return image.getAndroid().getUrl();
    }

    public String getTypeImageUrl(String type){
        if(type == null || type.equals(""))
        {
            return getUrl();
        }
        else if(type.equals("thumb")){
            return getThumbUrl();
        }
        else if(type.equals("android")){
            return getAndroidUrl();
        }
        return getUrl();
    }
}
