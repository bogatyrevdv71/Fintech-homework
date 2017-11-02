package ru.fintech.db.hw1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GraphView graphView = findViewById(R.id.graphView);
        // Добавление точек конструктором
        float[][] arr = new float[][] {{.11f,2},{0.13f,4.5f}, {0.14f,3}};
        Graph g = new Graph(arr);
        // Добавление шагов
        g.setHorizontalSteps(true);
        g.setVerticalSteps(true);
        //Изменение точек на лету
        g.addPoints(arr);
        // Добавление названия графика
        g.setTitle("График");
        // Добавление подписей осей
        g.setHorizontalLabel("Ось OX");
        g.setVerticalLabel("Ось OY");
        // Удаление подписей осей
        // g.setVerticalLabel(null);
        // Задание цвета линии
        g.setColor(0xACAB1337);
        graphView.setGraph(g);
    }
}
