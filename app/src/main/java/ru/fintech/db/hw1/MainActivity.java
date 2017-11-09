package ru.fintech.db.hw1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(DateExample.class, new DateTimeSerializer());
        gson.registerTypeAdapter(NamedMap.class, new NamedMapDeserializer());
        gson.registerTypeAdapter(Money.class, new MoneyDeserializer());
        gson.registerTypeAdapter(AlmostUseless.class, new AlmostUselessSerializer());

        AlmostUseless task1 = new AlmostUseless(1,"%DEFAULT_NAME%","useless",14,37);
        String task2 = "{\"name\":\"name\",\"any_map\":{\"a\":\"55\",\"b\":\"85\",\"c\":\"56\"}}";
        String task3 = "{\"money_amount\":\"2444,88\"}";
        DateExample task4 = new DateExample(new Date());

        Gson jsonConverter = gson.create();


        String answer1 = jsonConverter.toJson(task1);
        NamedMap answer2 = jsonConverter.fromJson(task2, NamedMap.class);
        Money answer3 = jsonConverter.fromJson(task3, Money.class);
        String answer4 = jsonConverter.toJson(task4);

        TextView firstTaskTextView  = findViewById(R.id.firstTaskTextField);
        firstTaskTextView.setText(answer1);

        TextView secondTaskTextView  = findViewById(R.id.secondTaskTextField);
        secondTaskTextView.setText(answer2.getAnyMap().getClass().toString());

        TextView thirdTaskTextView  = findViewById(R.id.thirdTaskTextField);
        String answer3AsAString = answer3.getMoney_amount().getClass().toString() + ' ' +
                answer3.getMoney_amount().toString();
        thirdTaskTextView.setText(answer3AsAString);

        TextView fourthTaskTextView = findViewById(R.id.fourthTaskTextField);
        fourthTaskTextView.setText(answer4);
    }
    private class AlmostUseless {
        private int id;
        private String name;
        private String uselessParam;
        private int trash_1;
        private int trash_2;

        AlmostUseless (int id, String name, String uselessParam, int trash_1, int trash_2) {
            this.id=id;
            this.name=name;
            this.uselessParam=uselessParam;
            this.trash_1=trash_1;
            this.trash_2=trash_2;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUselessParam() {
            return uselessParam;
        }

        public void setUselessParam(String uselessParam) {
            this.uselessParam = uselessParam;
        }

        public int getTrash_1() {
            return trash_1;
        }

        public void setTrash_1(int trash_1) {
            this.trash_1 = trash_1;
        }

        public int getTrash_2() {
            return trash_2;
        }

        public void setTrash_2(int trash_2) {
            this.trash_2 = trash_2;
        }
    }
    private class AlmostUselessSerializer implements JsonSerializer<AlmostUseless> {
        public JsonElement serialize(AlmostUseless src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("id", new JsonPrimitive(src.getId()));
            jsonObject.add("name", new JsonPrimitive(src.getName()));
            return jsonObject;
        }
    }

    private class NamedMap{
        private String name;
        private Map anyMap;

        NamedMap (String name, Map anyMap) {
            this.name = name;
            this.anyMap = anyMap;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        Map getAnyMap() {
            return anyMap;
        }

        public void setAnyMap(Map anyMap) {
            this.anyMap = anyMap;
        }
    }

    private class NamedMapDeserializer implements JsonDeserializer<NamedMap> {
        public NamedMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            String name = obj.get("name").getAsString();
            HashMap map = context.deserialize(obj.get("any_map"), HashMap.class);
            return new NamedMap(name, map);
        }
    }

    class Money {
        private BigDecimal money_amount;

        Money (BigDecimal money_amount) {
            this.money_amount = money_amount;
        }

        BigDecimal getMoney_amount() {
            return money_amount;
        }

        public void setMoney_amount(BigDecimal money_amount) {
            this.money_amount = money_amount;
        }
    }

    private class MoneyDeserializer implements JsonDeserializer<Money> {
        public Money deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return new Money(BigDecimal.valueOf(Double.parseDouble(json.getAsJsonObject().
                    get("money_amount").getAsString().replace(',','.'))));
        }
    }



    class DateExample {

        private Date date;

        DateExample(Date date) {

            this.date = date;

        }

        Date getDate() {

            return date;

        }

    }

    private class DateTimeSerializer implements JsonSerializer<DateExample> {
        public JsonElement serialize(DateExample src, Type typeOfSrc, JsonSerializationContext context) {
            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
            return new JsonPrimitive(dt1.format(src.getDate()));
        }
    }

}
