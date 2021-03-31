# JSONObject

```java
// https://mvnrepository.com/artifact/org.json/json
implementation group: 'org.json', name: 'json', version: '20180813'

```
`Project01_B.java`
```java
   // JSON - Java(org.json)
        JSONArray students = new JSONArray();

        JSONObject student = new JSONObject();
        student.put("name" , "홍길동");
        student.put("phone" , "010-1111-1111");
        student.put("address","서울");

        students.put(student);

        student = new JSONObject();
        student.put("name" , "나길동");
        student.put("phone" , "010-2222-2222");
        student.put("address","부산");

        students.put(student);
        JSONObject object = new JSONObject();

        object.put("students" , students);

        System.out.println("student : " + student);
        System.out.println("students : " +students);
        System.out.println("object : " +object.toString(2));
```

`Project01_C.java`

```java
 public static void main(String[] args) {

        String src = "info.json";
        // IO -> Stream(스트림)
        InputStream is = Project01_C.class.getClass().getResourceAsStream(src);
        if (is == null){
            throw new NullPointerException("파일을 찾을 수 없습니다.");
        }

        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        JSONArray students = object.getJSONArray("students");
        for (int i=0; i< students.length(); i++){
            JSONObject student = (JSONObject) students.get(i);
            System.out.print(student.get("name") +"\t");
            System.out.print(student.get("address") +"\t");
            System.out.println(student.get("phone") +"\t");

        }

    }

```