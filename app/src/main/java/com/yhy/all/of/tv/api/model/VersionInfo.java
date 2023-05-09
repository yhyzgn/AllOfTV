package com.yhy.all.of.tv.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 版本信息
 * <p>
 * Created on 2023-01-26 01:29
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class VersionInfo implements Serializable {

    /**
     * url : https://api.github.com/repos/yhyzgn/AllOfTV/releases/69116379
     * assets_url : https://api.github.com/repos/yhyzgn/AllOfTV/releases/69116379/assets
     * upload_url : https://uploads.github.com/repos/yhyzgn/AllOfTV/releases/69116379/assets{?name,label}
     * html_url : https://github.com/yhyzgn/AllOfTV/releases/tag/v1.0.11
     * id : 69116379
     * author : {"login":"github-actions[bot]","id":41898282,"node_id":"MDM6Qm90NDE4OTgyODI=","avatar_url":"https://avatars.githubusercontent.com/in/15368?v=4","gravatar_id":"","url":"https://api.github.com/users/github-actions%5Bbot%5D","html_url":"https://github.com/apps/github-actions","followers_url":"https://api.github.com/users/github-actions%5Bbot%5D/followers","following_url":"https://api.github.com/users/github-actions%5Bbot%5D/following{/other_user}","gists_url":"https://api.github.com/users/github-actions%5Bbot%5D/gists{/gist_id}","starred_url":"https://api.github.com/users/github-actions%5Bbot%5D/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/github-actions%5Bbot%5D/subscriptions","organizations_url":"https://api.github.com/users/github-actions%5Bbot%5D/orgs","repos_url":"https://api.github.com/users/github-actions%5Bbot%5D/repos","events_url":"https://api.github.com/users/github-actions%5Bbot%5D/events{/privacy}","received_events_url":"https://api.github.com/users/github-actions%5Bbot%5D/received_events","type":"Bot","site_admin":false}
     * node_id : RE_kwDOHblaJc4EHqHb
     * tag_name : v1.0.11
     * target_commitish : main
     * name : v1.0.11
     * draft : false
     * prerelease : false
     * created_at : 2022-06-10T02:33:24Z
     * published_at : 2022-06-10T02:36:28Z
     * assets : [{"url":"https://api.github.com/repos/yhyzgn/AllOfTV/releases/assets/68060132","id":68060132,"node_id":"RA_kwDOHblaJc4EDoPk","name":"AllOfTV-linux-amd64-v1.0.11_40.tar.gz","label":"","uploader":{"login":"github-actions[bot]","id":41898282,"node_id":"MDM6Qm90NDE4OTgyODI=","avatar_url":"https://avatars.githubusercontent.com/in/15368?v=4","gravatar_id":"","url":"https://api.github.com/users/github-actions%5Bbot%5D","html_url":"https://github.com/apps/github-actions","followers_url":"https://api.github.com/users/github-actions%5Bbot%5D/followers","following_url":"https://api.github.com/users/github-actions%5Bbot%5D/following{/other_user}","gists_url":"https://api.github.com/users/github-actions%5Bbot%5D/gists{/gist_id}","starred_url":"https://api.github.com/users/github-actions%5Bbot%5D/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/github-actions%5Bbot%5D/subscriptions","organizations_url":"https://api.github.com/users/github-actions%5Bbot%5D/orgs","repos_url":"https://api.github.com/users/github-actions%5Bbot%5D/repos","events_url":"https://api.github.com/users/github-actions%5Bbot%5D/events{/privacy}","received_events_url":"https://api.github.com/users/github-actions%5Bbot%5D/received_events","type":"Bot","site_admin":false},"content_type":"application/gzip","state":"uploaded","size":20307467,"download_count":4,"created_at":"2022-06-10T02:36:59Z","updated_at":"2022-06-10T02:37:01Z","browser_download_url":"https://github.com/yhyzgn/AllOfTV/releases/download/v1.0.11/AllOfTV-linux-amd64-v1.0.11_40.tar.gz"},{"url":"https://api.github.com/repos/yhyzgn/AllOfTV/releases/assets/68060131","id":68060131,"node_id":"RA_kwDOHblaJc4EDoPj","name":"AllOfTV-linux-amd64-with-ffmpeg-v1.0.11_40.tar.gz","label":"","uploader":{"login":"github-actions[bot]","id":41898282,"node_id":"MDM6Qm90NDE4OTgyODI=","avatar_url":"https://avatars.githubusercontent.com/in/15368?v=4","gravatar_id":"","url":"https://api.github.com/users/github-actions%5Bbot%5D","html_url":"https://github.com/apps/github-actions","followers_url":"https://api.github.com/users/github-actions%5Bbot%5D/followers","following_url":"https://api.github.com/users/github-actions%5Bbot%5D/following{/other_user}","gists_url":"https://api.github.com/users/github-actions%5Bbot%5D/gists{/gist_id}","starred_url":"https://api.github.com/users/github-actions%5Bbot%5D/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/github-actions%5Bbot%5D/subscriptions","organizations_url":"https://api.github.com/users/github-actions%5Bbot%5D/orgs","repos_url":"https://api.github.com/users/github-actions%5Bbot%5D/repos","events_url":"https://api.github.com/users/github-actions%5Bbot%5D/events{/privacy}","received_events_url":"https://api.github.com/users/github-actions%5Bbot%5D/received_events","type":"Bot","site_admin":false},"content_type":"application/gzip","state":"uploaded","size":53844227,"download_count":4,"created_at":"2022-06-10T02:36:59Z","updated_at":"2022-06-10T02:37:01Z","browser_download_url":"https://github.com/yhyzgn/AllOfTV/releases/download/v1.0.11/AllOfTV-linux-amd64-with-ffmpeg-v1.0.11_40.tar.gz"},{"url":"https://api.github.com/repos/yhyzgn/AllOfTV/releases/assets/68060119","id":68060119,"node_id":"RA_kwDOHblaJc4EDoPX","name":"AllOfTV-mac-amd64-v1.0.11_40.tar.gz","label":"","uploader":{"login":"github-actions[bot]","id":41898282,"node_id":"MDM6Qm90NDE4OTgyODI=","avatar_url":"https://avatars.githubusercontent.com/in/15368?v=4","gravatar_id":"","url":"https://api.github.com/users/github-actions%5Bbot%5D","html_url":"https://github.com/apps/github-actions","followers_url":"https://api.github.com/users/github-actions%5Bbot%5D/followers","following_url":"https://api.github.com/users/github-actions%5Bbot%5D/following{/other_user}","gists_url":"https://api.github.com/users/github-actions%5Bbot%5D/gists{/gist_id}","starred_url":"https://api.github.com/users/github-actions%5Bbot%5D/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/github-actions%5Bbot%5D/subscriptions","organizations_url":"https://api.github.com/users/github-actions%5Bbot%5D/orgs","repos_url":"https://api.github.com/users/github-actions%5Bbot%5D/repos","events_url":"https://api.github.com/users/github-actions%5Bbot%5D/events{/privacy}","received_events_url":"https://api.github.com/users/github-actions%5Bbot%5D/received_events","type":"Bot","site_admin":false},"content_type":"application/gzip","state":"uploaded","size":23202599,"download_count":3,"created_at":"2022-06-10T02:36:28Z","updated_at":"2022-06-10T02:36:29Z","browser_download_url":"https://github.com/yhyzgn/AllOfTV/releases/download/v1.0.11/AllOfTV-mac-amd64-v1.0.11_40.tar.gz"},{"url":"https://api.github.com/repos/yhyzgn/AllOfTV/releases/assets/68060120","id":68060120,"node_id":"RA_kwDOHblaJc4EDoPY","name":"AllOfTV-mac-amd64-with-ffmpeg-v1.0.11_40.tar.gz","label":"","uploader":{"login":"github-actions[bot]","id":41898282,"node_id":"MDM6Qm90NDE4OTgyODI=","avatar_url":"https://avatars.githubusercontent.com/in/15368?v=4","gravatar_id":"","url":"https://api.github.com/users/github-actions%5Bbot%5D","html_url":"https://github.com/apps/github-actions","followers_url":"https://api.github.com/users/github-actions%5Bbot%5D/followers","following_url":"https://api.github.com/users/github-actions%5Bbot%5D/following{/other_user}","gists_url":"https://api.github.com/users/github-actions%5Bbot%5D/gists{/gist_id}","starred_url":"https://api.github.com/users/github-actions%5Bbot%5D/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/github-actions%5Bbot%5D/subscriptions","organizations_url":"https://api.github.com/users/github-actions%5Bbot%5D/orgs","repos_url":"https://api.github.com/users/github-actions%5Bbot%5D/repos","events_url":"https://api.github.com/users/github-actions%5Bbot%5D/events{/privacy}","received_events_url":"https://api.github.com/users/github-actions%5Bbot%5D/received_events","type":"Bot","site_admin":false},"content_type":"application/gzip","state":"uploaded","size":48109351,"download_count":2,"created_at":"2022-06-10T02:36:28Z","updated_at":"2022-06-10T02:36:30Z","browser_download_url":"https://github.com/yhyzgn/AllOfTV/releases/download/v1.0.11/AllOfTV-mac-amd64-with-ffmpeg-v1.0.11_40.tar.gz"},{"url":"https://api.github.com/repos/yhyzgn/AllOfTV/releases/assets/68060171","id":68060171,"node_id":"RA_kwDOHblaJc4EDoQL","name":"AllOfTV-windows-amd64-v1.0.11_40.tar.gz","label":"","uploader":{"login":"github-actions[bot]","id":41898282,"node_id":"MDM6Qm90NDE4OTgyODI=","avatar_url":"https://avatars.githubusercontent.com/in/15368?v=4","gravatar_id":"","url":"https://api.github.com/users/github-actions%5Bbot%5D","html_url":"https://github.com/apps/github-actions","followers_url":"https://api.github.com/users/github-actions%5Bbot%5D/followers","following_url":"https://api.github.com/users/github-actions%5Bbot%5D/following{/other_user}","gists_url":"https://api.github.com/users/github-actions%5Bbot%5D/gists{/gist_id}","starred_url":"https://api.github.com/users/github-actions%5Bbot%5D/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/github-actions%5Bbot%5D/subscriptions","organizations_url":"https://api.github.com/users/github-actions%5Bbot%5D/orgs","repos_url":"https://api.github.com/users/github-actions%5Bbot%5D/repos","events_url":"https://api.github.com/users/github-actions%5Bbot%5D/events{/privacy}","received_events_url":"https://api.github.com/users/github-actions%5Bbot%5D/received_events","type":"Bot","site_admin":false},"content_type":"application/gzip","state":"uploaded","size":24277846,"download_count":6,"created_at":"2022-06-10T02:38:44Z","updated_at":"2022-06-10T02:38:47Z","browser_download_url":"https://github.com/yhyzgn/AllOfTV/releases/download/v1.0.11/AllOfTV-windows-amd64-v1.0.11_40.tar.gz"},{"url":"https://api.github.com/repos/yhyzgn/AllOfTV/releases/assets/68060172","id":68060172,"node_id":"RA_kwDOHblaJc4EDoQM","name":"AllOfTV-windows-amd64-with-ffmpeg-v1.0.11_40.tar.gz","label":"","uploader":{"login":"github-actions[bot]","id":41898282,"node_id":"MDM6Qm90NDE4OTgyODI=","avatar_url":"https://avatars.githubusercontent.com/in/15368?v=4","gravatar_id":"","url":"https://api.github.com/users/github-actions%5Bbot%5D","html_url":"https://github.com/apps/github-actions","followers_url":"https://api.github.com/users/github-actions%5Bbot%5D/followers","following_url":"https://api.github.com/users/github-actions%5Bbot%5D/following{/other_user}","gists_url":"https://api.github.com/users/github-actions%5Bbot%5D/gists{/gist_id}","starred_url":"https://api.github.com/users/github-actions%5Bbot%5D/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/github-actions%5Bbot%5D/subscriptions","organizations_url":"https://api.github.com/users/github-actions%5Bbot%5D/orgs","repos_url":"https://api.github.com/users/github-actions%5Bbot%5D/repos","events_url":"https://api.github.com/users/github-actions%5Bbot%5D/events{/privacy}","received_events_url":"https://api.github.com/users/github-actions%5Bbot%5D/received_events","type":"Bot","site_admin":false},"content_type":"application/gzip","state":"uploaded","size":56597665,"download_count":13,"created_at":"2022-06-10T02:38:44Z","updated_at":"2022-06-10T02:38:48Z","browser_download_url":"https://github.com/yhyzgn/AllOfTV/releases/download/v1.0.11/AllOfTV-windows-amd64-with-ffmpeg-v1.0.11_40.tar.gz"}]
     * tarball_url : https://api.github.com/repos/yhyzgn/AllOfTV/tarball/v1.0.11
     * zipball_url : https://api.github.com/repos/yhyzgn/AllOfTV/zipball/v1.0.11
     * body : Updated Release Body
     */

    @SerializedName("url")
    public String url;
    @SerializedName("assets_url")
    public String assetsUrl;
    @SerializedName("upload_url")
    public String uploadUrl;
    @SerializedName("html_url")
    public String htmlUrl;
    @SerializedName("id")
    public Integer id;
    @SerializedName("author")
    public AuthorBean author;
    @SerializedName("node_id")
    public String nodeId;
    @SerializedName("tag_name")
    public String tagName;
    @SerializedName("target_commitish")
    public String targetCommitish;
    @SerializedName("name")
    public String name;
    @SerializedName("draft")
    public Boolean draft;
    @SerializedName("prerelease")
    public Boolean prerelease;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("published_at")
    public String publishedAt;
    @SerializedName("tarball_url")
    public String tarballUrl;
    @SerializedName("zipball_url")
    public String zipballUrl;
    @SerializedName("body")
    public String body;
    @SerializedName("assets")
    public List<AssetsBean> assets;

    public static class AuthorBean implements Serializable {
        /**
         * login : github-actions[bot]
         * id : 41898282
         * node_id : MDM6Qm90NDE4OTgyODI=
         * avatar_url : https://avatars.githubusercontent.com/in/15368?v=4
         * gravatar_id :
         * url : https://api.github.com/users/github-actions%5Bbot%5D
         * html_url : https://github.com/apps/github-actions
         * followers_url : https://api.github.com/users/github-actions%5Bbot%5D/followers
         * following_url : https://api.github.com/users/github-actions%5Bbot%5D/following{/other_user}
         * gists_url : https://api.github.com/users/github-actions%5Bbot%5D/gists{/gist_id}
         * starred_url : https://api.github.com/users/github-actions%5Bbot%5D/starred{/owner}{/repo}
         * subscriptions_url : https://api.github.com/users/github-actions%5Bbot%5D/subscriptions
         * organizations_url : https://api.github.com/users/github-actions%5Bbot%5D/orgs
         * repos_url : https://api.github.com/users/github-actions%5Bbot%5D/repos
         * events_url : https://api.github.com/users/github-actions%5Bbot%5D/events{/privacy}
         * received_events_url : https://api.github.com/users/github-actions%5Bbot%5D/received_events
         * type : Bot
         * site_admin : false
         */

        @SerializedName("login")
        public String login;
        @SerializedName("id")
        public Integer id;
        @SerializedName("node_id")
        public String nodeId;
        @SerializedName("avatar_url")
        public String avatarUrl;
        @SerializedName("gravatar_id")
        public String gravatarId;
        @SerializedName("url")
        public String url;
        @SerializedName("html_url")
        public String htmlUrl;
        @SerializedName("followers_url")
        public String followersUrl;
        @SerializedName("following_url")
        public String followingUrl;
        @SerializedName("gists_url")
        public String gistsUrl;
        @SerializedName("starred_url")
        public String starredUrl;
        @SerializedName("subscriptions_url")
        public String subscriptionsUrl;
        @SerializedName("organizations_url")
        public String organizationsUrl;
        @SerializedName("repos_url")
        public String reposUrl;
        @SerializedName("events_url")
        public String eventsUrl;
        @SerializedName("received_events_url")
        public String receivedEventsUrl;
        @SerializedName("type")
        public String type;
        @SerializedName("site_admin")
        public Boolean siteAdmin;
    }

    public static class AssetsBean implements Serializable {
        /**
         * url : https://api.github.com/repos/yhyzgn/AllOfTV/releases/assets/68060132
         * id : 68060132
         * node_id : RA_kwDOHblaJc4EDoPk
         * name : AllOfTV-linux-amd64-v1.0.11_40.tar.gz
         * label :
         * uploader : {"login":"github-actions[bot]","id":41898282,"node_id":"MDM6Qm90NDE4OTgyODI=","avatar_url":"https://avatars.githubusercontent.com/in/15368?v=4","gravatar_id":"","url":"https://api.github.com/users/github-actions%5Bbot%5D","html_url":"https://github.com/apps/github-actions","followers_url":"https://api.github.com/users/github-actions%5Bbot%5D/followers","following_url":"https://api.github.com/users/github-actions%5Bbot%5D/following{/other_user}","gists_url":"https://api.github.com/users/github-actions%5Bbot%5D/gists{/gist_id}","starred_url":"https://api.github.com/users/github-actions%5Bbot%5D/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/github-actions%5Bbot%5D/subscriptions","organizations_url":"https://api.github.com/users/github-actions%5Bbot%5D/orgs","repos_url":"https://api.github.com/users/github-actions%5Bbot%5D/repos","events_url":"https://api.github.com/users/github-actions%5Bbot%5D/events{/privacy}","received_events_url":"https://api.github.com/users/github-actions%5Bbot%5D/received_events","type":"Bot","site_admin":false}
         * content_type : application/gzip
         * state : uploaded
         * size : 20307467
         * download_count : 4
         * created_at : 2022-06-10T02:36:59Z
         * updated_at : 2022-06-10T02:37:01Z
         * browser_download_url : https://github.com/yhyzgn/AllOfTV/releases/download/v1.0.11/AllOfTV-linux-amd64-v1.0.11_40.tar.gz
         */

        @SerializedName("url")
        public String url;
        @SerializedName("id")
        public Integer id;
        @SerializedName("node_id")
        public String nodeId;
        @SerializedName("name")
        public String name;
        @SerializedName("label")
        public String label;
        @SerializedName("uploader")
        public AuthorBean uploader;
        @SerializedName("content_type")
        public String contentType;
        @SerializedName("state")
        public String state;
        @SerializedName("size")
        public Integer size;
        @SerializedName("download_count")
        public Integer downloadCount;
        @SerializedName("created_at")
        public String createdAt;
        @SerializedName("updated_at")
        public String updatedAt;
        @SerializedName("browser_download_url")
        public String browserDownloadUrl;
    }
}
