package huolto.kirja;

public class ServiceInfo {

    String service;
    String kilometers;
    String information;
    String date;

    public ServiceInfo() {

    }

    public ServiceInfo(String service, String kilometers, String information, String date) {
        this.service = service;
        this.kilometers = kilometers;
        this.information = information;
        this.date = date;
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

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
