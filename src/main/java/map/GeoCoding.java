package map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class GeoCoding {
    public static void main(String[] args) {
        String client_id = "po0v5t31yt";
        String client_secret = "HJOhVIE8SRGzxyPHOydT3EthxAeo3yCykBu69S8Z";

        // 프로그램이 데이터를 입력 받을 때 -> InputStream
        BufferedReader io = new BufferedReader(new InputStreamReader(System.in));
        // 문자 단위 입출력을 위한 하위 스트림 -> BufferedReader
        try {
            System.out.println("주소를 입력하세요");
            String address = io.readLine(); // 콘솔에서 입력한 것을 문자열 변수에 저장
            String addr = URLEncoder.encode(address,"UTF-8"); // 입력 공백도 문자처리 해주기 위해 인코딩!!!
            // 주소의 텍스트를 입력 받아 좌표를 포함한 상세 정보들을 제공합니다.
            String reqUrl="https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query="+addr;

            URL url = new URL(reqUrl); // URL이 정확한 URL 인지 확인하기 위해 URL 객체 생성
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // 정상적인 URL 이면 이제 서버와 연결을 시도한다.

            // URL 에 대한 정보를 담는다.
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID",client_id);
            con.setRequestProperty("X-NCP-APIGW-API-KEY",client_secret);
            BufferedReader br;
            int responseCode = con.getResponseCode(); // 200
            if (responseCode == 200){
                // 정상적인 호출이면
                br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
                        // con이 네이버 지도 API 와 연결되어 있기 때문에 거기서 데이터를 꺼내기 위해
                        // getInputStream 을 사용한다.
                // 숫자나 한글을 포함한 문자 데이터이기 때문에 BufferedReader 가 필요하다.
            }else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            // 서버에서 내려주는 데이터를 받는다.
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = br.readLine()) != null){
                // 데이터를 한 줄 씩 읽어서 StringBuffer 에 넣어준다.
                response.append(line);
            }
            // StringBuffer에 모든 데이터가 Json 형태로 문자열로 저장된다.
            br.close();

            // StringBuffer 에 데이터를 JSON 객체로 변경한다.
            JSONTokener tokener = new JSONTokener(response.toString());
            JSONObject object = new JSONObject(tokener);
            System.out.println("object.toString() = " +object.toString());

            JSONArray arr = object.getJSONArray("addresses");
            for (int i=0; i< arr.length(); i++){
                JSONObject temp = (JSONObject) arr.get(i);
                System.out.println("address : " + temp.get("roadAddress"));
                System.out.println("jibunAddress : " +temp.get("jibunAddress"));
                System.out.println("경도: "+temp.get("x"));
                System.out.println("위도: "+temp.get("y"));
            }
        } catch (Exception e) {

        }
    }
}
