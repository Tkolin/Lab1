package com.example.lab1;

import static android.widget.Toast.makeText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //private ActivityMainBinding binding;
    private LinearLayout board;
    private ArrayList<Button> squares = new ArrayList<>();

    private  boolean startGame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioButton oBtn = findViewById(R.id.CrestBTN);
        RadioButton xBtn = findViewById(R.id.KrugBTN);

        View.OnClickListener listener = (view) -> {
            Button btn = (Button) view;
            // Если текст на кнопке не пустой, то выходим из события
            if (!btn.getText().toString().equals("")) return;

            if(!startGame) {
                startGame = true;
                if (oBtn.isChecked())
                    GameManager.isTurn = true;
                else
                    GameManager.isTurn = false;
            }
            // Устанавливаем либо X, либо O в зависимости от хода
            if (GameManager.isTurn) {
                btn.setText(GameManager.firstSymbol);
                int[] comb = calcWinnPositions(GameManager.firstSymbol);
                if (comb != null) {
                    highlightWinningCombination(comb);
                    Toast.makeText(
                            getApplicationContext(),
                            "Победитель: " + GameManager.firstSymbol,
                            Toast.LENGTH_LONG).show();

                }
            } else {
                btn.setText(GameManager.secondSymbol);
                int[] comb = calcWinnPositions(GameManager.secondSymbol);
                if (comb != null) {
                    highlightWinningCombination(comb);
                    Toast.makeText(
                            getApplicationContext(),
                            "Победитель: " + GameManager.secondSymbol,
                            Toast.LENGTH_LONG).show();
                }
            }
            // Меняем очередность хода: true -> false
            GameManager.isTurn = !GameManager.isTurn;
            if(GameManager.isTurn)
                xBtn.setActivated(true);
            else
                oBtn.setActivated(true);
        };

        board = findViewById(R.id.board);
        generateBoard(3, 3, board);
        setListenerToSquares(listener);
        initClearBoardBtn();
    }

    private void initClearBoardBtn() {
        Button clearBtn = findViewById(R.id.clear_board_value);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        getApplicationContext(),
                        "Новая игра",
                        Toast.LENGTH_LONG).show();

                for (Button square : squares) {
                    square.setText("");
                    square.setBackgroundTintList(
                            ContextCompat.getColorStateList(
                                    getApplicationContext(),
                                    R.color.grey));

                    startGame = false;
                }
            }
        });
    }

    public void generateBoard(int rowCount, int columnCount, LinearLayout board) {
        // Генерация строк от 0 до rowCount
        for (int row = 0; row < rowCount; row++) {
            // Создаем контейнер (нашу строку) и добавляем его в board
            LinearLayout rowContainer = generateRow(columnCount);
            board.addView(rowContainer);
        }
    }

    // Устанавливаем слушателя всем кнопкам
    private void setListenerToSquares(View.OnClickListener listener) {
        for (int i = 0; i < squares.size(); i++) {
            // Получаем кнопку из списка и устанавливаем ей слушателя событий
            squares.get(i).setOnClickListener(listener);
        }
    }
    // Метод выделение победной комбинации зелёным
    public void highlightWinningCombination(int[] winningCombination) {
        for (int index : winningCombination) {
            Button square = squares.get(index);
            square.setBackgroundTintList(ContextCompat.getColorStateList(
                    getApplicationContext(),
                    R.color.greed));
        }
    }

    // Метод генерации строк для доски
    private LinearLayout generateRow(int squaresCount) {
        // Создаем контейнер (строку), который будет возвращен с кнопками
        LinearLayout rowContainer = new LinearLayout(getApplicationContext());
        rowContainer.setOrientation(LinearLayout.HORIZONTAL);
        rowContainer.setLayoutParams(
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        for (int square = 0; square < squaresCount; square++) {
            // Создаем кнопку для добавления в строку
            Button button = new Button(getApplicationContext());
            // Устанавливаем цвет с помощью tint
            button.setBackgroundTintList(
                    ContextCompat.getColorStateList(
                            getApplicationContext(),
                            R.color.grey));
            button.setWidth(convertToPixel(50));
            button.setHeight(convertToPixel(90));
            rowContainer.addView(button); // Добавляем кнопку в строку
            squares.add(button);
        }
        return rowContainer;
    }

    public int convertToPixel(int digit) {
        float density = getApplicationContext()
                .getResources().getDisplayMetrics().density;
        return (int) (digit * density + 0.5); // Перевод в пиксели
    }

    public int[] calcWinnPositions(String symbol) {
        // Перебираем все комбинации
        for (int i = 0; i < GameManager.winCombination.length; i++) {
            // Найдена ли комбинация
            boolean findComb = true;
            // Перебираем все три символа, например, [0, 1, 2]
            for (int j = 0; j < GameManager.winCombination[0].length; j++) {
                int index = GameManager.winCombination[i][j]; // 0, 1, 2
                // Проверяем, имеет ли кнопка с индексом index в себе символ или нет
                if (!squares.get(index).getText().toString().equals(symbol)) {
                    // Если нет, то комбинация не является выигрышной
                    findComb = false;
                    break;
                }
            }
            // Если комбинация осталась true, то она выигрышная
            if (findComb) return GameManager.winCombination[i];
        }
        return null;
    }
}
