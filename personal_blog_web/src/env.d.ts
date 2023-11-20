/// <reference types="vite/client" />
interface ImportMetaEnv {
    readonly BLOG_REQUEST_URL:string;
    readonly BLOG_WebSocket_URi:string
    readonly BLOG_REQUEST_HEADERS_TOKEN_NAME:string;
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}
