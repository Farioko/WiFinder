package se.miun.student.faba1500.sakfinnare;

import java.net.URL;

enum HTTPRequestType{GET, PUT};

public class HTTPRequest {
    public URL url;
    public HTTPRequestType type;
    public boolean lost;
}
