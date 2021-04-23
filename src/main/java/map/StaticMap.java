package map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

public class StaticMap {
    // 지도 이미지 생성 메서드
    public static void map_service(String point_x , String point_y ,String address){
        String client_id = "po0v5t31yt";
        String client_secret = "HJOhVIE8SRGzxyPHOydT3EthxAeo3yCykBu69S8Z";
        String URL_STATICMAP = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?";

        try {
            String pos = URLEncoder.encode(point_x+" "+point_y,"UTF-8");
            String url = URL_STATICMAP;
            url += "center="+point_x+","+point_y;
            url += "&level=12&w=700&h=500";
            url += "&markers=type:t|size:mid|pos:"+pos+"|label:"+URLEncoder.encode(address,"UTF-8");
            URL u = new URL(url);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID",client_id);
            con.setRequestProperty("X-NCP-APIGW-API-KEY",client_secret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode ==200){
                InputStream is = con.getInputStream();
                int read = 0;
                byte[] bytes = new byte[1024];
                // 랜덤한 이름으로 파일 생성
                String tempname = Long.valueOf(new Date().getTime()).toString();
                File f = new File("C:\\Temp/"+tempname+".jpg");
                f.createNewFile();
                OutputStream outputStream = new FileOutputStream(f);
                while ((read = is.read(bytes)) != -1){
                    outputStream.write(bytes,0,read);
                }
                is.close();
            }else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine() ) != null){
                    response.append(inputLine);

                }
                br.close();
                System.out.println(response.toString());
            }
        } catch (Exception e) {

            System.out.println(e);
        }
    }

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
            String x =""; String y=""; String z = "";
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
                x = (String) temp.get("x");
                y = (String) temp.get("y");
                z = (String) temp.get("roadAddress");
            }
            // 추가된 부분분
            map_service(x,y,z);
        } catch (Exception e) {

        }
    }
}
