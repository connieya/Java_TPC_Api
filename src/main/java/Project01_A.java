import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kr.inflearn.BookDTO;

import java.util.ArrayList;
import java.util.List;

public class Project01_A {

    public static void main(String[] args) {

        BookDTO dto = new BookDTO("자바",21000,"에이콘",670);

        Gson gson = new Gson();

        // 객체  -> json
        // Object(BookDTO) -> JSON(String)
        String json = gson.toJson(dto);
        System.out.println(json);

        // JSON(String) -> Object(BookDTO)
        BookDTO dto1 = gson.fromJson(json , BookDTO.class);
        System.out.println(dto1);


        // List - > 배열 객체(json)

        // Object(List<BookDTO>) -> JSON(String)
        List<BookDTO> list = new ArrayList<>();

        list.add(new BookDTO("자바",21000,"에이콘",670));
        list.add(new BookDTO("파이썬",11000,"에이콘",170));
        list.add(new BookDTO("스프링부트",25000,"에이콘",970));

        System.out.println("list : "  + list.get(0));

        String listToJson = gson.toJson(list);
        System.out.println("listToJson : " +listToJson);

        //JSON(String)  -> Object(List<BookDTO>)
        List<BookDTO> list1 = gson.fromJson(listToJson , new TypeToken<List<BookDTO>>(){}.getType());
        for (BookDTO bookDTO : list1) {
            System.out.println("bookDTO : " +bookDTO);

        }
    }
}
