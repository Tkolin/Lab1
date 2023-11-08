package com.example.lab1;

//Класс для работы с игрой
public  class GameManager {
        public static boolean isTurn = true;//true->X, false->O
        //Обозначение знаков на экране
        public static String firstSymbol = "X"; 
        public static String secondSymbol = "O";
        // Сетка выйгрышных комбинаций
        public static int[][] winCombination =
                {
                        {0,1,2}, {3,4,5}, {6,7,8},
                        {0,3,6}, {1,4,7}, {2,5,8},
                        {0,4,8},{2,4,6}
                };

    }

