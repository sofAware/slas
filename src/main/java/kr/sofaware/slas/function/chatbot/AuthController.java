package kr.sofaware.slas.function.chatbot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class AuthController {

    @PostMapping("test")
    @ResponseBody
    public HashMap<String,Object> test(@RequestBody Map<String, Object> params) throws JsonProcessingException {

        HashMap<String, Object> resultJson = new HashMap<>();

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(params);
        System.out.println(jsonInString);

        List<HashMap<String,Object>> outputs = new ArrayList<>();
        HashMap<String,Object> template = new HashMap<>();
        HashMap<String, Object> simpleText = new HashMap<>();
        HashMap<String, Object> text = new HashMap<>();

        text.put("text","AuthController에서 만든 메세지임당.");
        simpleText.put("simpleText",text);
        outputs.add(simpleText);

        template.put("outputs",outputs);

        resultJson.put("version","2.0");
        resultJson.put("template",template);

        return resultJson;
    }
}
