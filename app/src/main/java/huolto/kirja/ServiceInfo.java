package huolto.kirja;

public class ServiceInfo {

    String service;
    String kilometers;

    public ServiceInfo() {

    }

    public ServiceInfo(String service, String kilometers) {
        this.service = service;
        this.kilometers = kilometers;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getKilometers() {
        return kilometers;
    }

    public void setKilometers(String kilometers) {
        this.kilometers = kilometers;
    }
}
