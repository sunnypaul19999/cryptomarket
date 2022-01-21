package helios.wazirx.crypto;

class AccessPointLoopkup {

    private AccessPointLoopkup() {
    }

    public enum AccessPoint {
        DEPTH("https://api.wazirx.com/api/v2/depths"), TICKER("https://api.wazirx.com/uapi/v1/ticker/24hr");

        private final String accessPoint;

        private AccessPoint(String accessPoint) {
            this.accessPoint = accessPoint;
        }

        public String getURL() {
            return accessPoint;
        }
    }

    static AccessPoint getDEPTH() {
        return AccessPoint.DEPTH;
    }

    static AccessPoint getTICKER() {
        return AccessPoint.TICKER;
    }
}