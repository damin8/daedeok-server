package hours22.daedeokserver.file.dto;

public enum FileType {
    HANDOUT {
        public String directory() {
            return "handout/";
        }
    },
    CERTIFICATE {
        public String directory() {
            return "certificate/";
        }
    },
    QNA {
        public String directory() {
            return "qna/";
        }
    },
    LECTURE_CATEGORY {
        public String directory() {
            return "lecture-category/";
        }
    },
    ATTACHMENT {
        public String directory() {
            return "attachment/";
        }
    },
    LECTURE_BOARD {
        public String directory() {
            return "lecture-board/";
        }
    },
    GLOBAL_NOTICE {
        public String directory() {
            return "global-notice/";
        }
    },
    TUTOR_NOTICE {
        public String directory() {
            return "tutor-notice/";
        }
    },
    AC_INFO {
        public String directory() {
            return "ac-info/";
        }
    },
    FAQ {
        public String directory() {
            return "faq/";
        }
    },
    MAIN_IMAGE {
        public String directory() {
            return "main-image/";
        }
    },
    POPUP {
        public String directory() {
            return "popup/";
        }
    },
    DUMMY {
        public String directory() {
            return "dummy/";
        }
    };

    public abstract String directory();
}
