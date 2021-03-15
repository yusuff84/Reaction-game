/**
 * ПРОЧТИТЕ ФАЙЛ README ПЕРЕД ИЗУЧЕНИЕМ ДАННОГО КОДА
 */

package com.itcubegrozny.reactiungame;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Game extends AppCompatActivity {

    // счетчик
    private int count = 0;

    // интерфейс (ООП, не GUI) для сохранения настроек ПО, в нашем случае рекорда
    private SharedPreferences recordPreferences;
    private final String KEY = "record";

    // объявление View объектов из активити
    private TextView gameCount;
    private TextView txtView;

    private Button button_n1;
    private Button button_n2;
    private Button button_n3;
    private Button button_n4;
    private Button button_n5;
    private Button button_n6;
    private Button button_n7;
    private Button button_n8;
    private Button button_n9;

    // массив строк, в дальнейшем будем дергать для вывода в "красное окошко"
    private final String[] WORD = {"Один", "Два", "Три", "Четыре", "Пять", "Шесть", " Семь",
            "Восемь", "Девять"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal);

        // связка с объектами на активити
        button_n1 = findViewById(R.id.button_n1);
        button_n2 = findViewById(R.id.button_n2);
        button_n3 = findViewById(R.id.button_n3);
        button_n4 = findViewById(R.id.button_n4);
        button_n5 = findViewById(R.id.button_n5);
        button_n6 = findViewById(R.id.button_n6);
        button_n7 = findViewById(R.id.button_n7);
        button_n8 = findViewById(R.id.button_n8);
        button_n9 = findViewById(R.id.button_n9);

        txtView = findViewById(R.id.numbers_view);

//        gameLogic();

        // ставим случшателей на числовые (цифровые все они) кнопки
        button_n1.setOnClickListener(onClickListener);
        button_n2.setOnClickListener(onClickListener);
        button_n3.setOnClickListener(onClickListener);
        button_n4.setOnClickListener(onClickListener);
        button_n5.setOnClickListener(onClickListener);
        button_n6.setOnClickListener(onClickListener);
        button_n7.setOnClickListener(onClickListener);
        button_n8.setOnClickListener(onClickListener);
        button_n9.setOnClickListener(onClickListener);

        gameLogic();

        // про это уже сказано в классе MainActivity
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // обработка слушателей
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onClick(View view) {

            // узнаем какое слово было выведено на экран
            int index = (Arrays.asList(WORD).indexOf(txtView.getText())) + 1;

            Button btn = findViewById(view.getId());
            gameCount = findViewById(R.id.game_count);

            // если слово на экране соответствует тексту на нажатой кнопке...
            if (String.valueOf(index).contentEquals(btn.getText())) {

                // меняем кнопки
                setRandomShuffleValueButton();

                // выборка нового слово
                randomSelectWord();

                count++;
            } else {
                vibrate(getBaseContext());
                count--;

                /**
                 * закомментируйте 122-124 строки и посмотрите что получится
                 * обязательно отвечайте неправильно
                 */
                if (count < 0) {
                    count = 0;
                }
            }
            gameCount.setText("Счет: " + count);
        }
    };


    // для активации/деактивации числовых кнопок
    private void buttonsEnabled(boolean i) {
        button_n1.setEnabled(i);
        button_n2.setEnabled(i);
        button_n3.setEnabled(i);
        button_n4.setEnabled(i);
        button_n5.setEnabled(i);
        button_n6.setEnabled(i);
        button_n7.setEnabled(i);
        button_n8.setEnabled(i);
        button_n9.setEnabled(i);
    }


    /**
     * метод vibrate() для реализации вибрации
     * вызывается в 92 строке, при неправильном ответе
     * если нет вибратора в устройстве, то не сработает, но и не даст ошибку
     */
    private static void vibrate(Context c) {
        final int VIBRATE_TIME = 300;
        Vibrator vibrator = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_TIME, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(VIBRATE_TIME);
        }
    }


    /**
     * для сохранения рекорда в файле, чтобы в последующих запусках знать какой результат был последний раз
     * MODE_PRIVATE означает, что только данная программа имеет доступ к данному файлу
     */
    private void writeRecord() {
        recordPreferences = getSharedPreferences("myRecord", MODE_PRIVATE);
        SharedPreferences.Editor ed = recordPreferences.edit();
        ed.putString(KEY, Integer.toString(count));
        ed.apply();
    }

    // устанавливает звук
    private void sound(int id) {
        MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(), id);
        if (id == R.raw.soundwin) {
            mediaPlayer.start();
            mediaPlayer.setLooping(false);
        }
        if (id == R.raw.soundfail) {
            mediaPlayer.start();
            mediaPlayer.setLooping(false);
        }
    }

    private void gameLogic() {
        txtView.setEnabled(false);
        buttonsEnabled(true);

        randomSelectWord();///////////////////////

        TextView gameTime = findViewById(R.id.gameTime);


        /**
         * первый параметр в CountDownTimer устанавливает значение таймера
         * второй параметр время обновления таймера
         * поиграйте со сменой значений
         */
        new CountDownTimer(60000, 1000) {


            /**
             * в методе onTick пишем то, что нужно обновить пока работает таймер
             * в данном примере мы обновляем значение таймера на экране (стр. 176)
             * @param l возвращает время в миллисекундах, 1 секунда = 1000 миллисекунд
             */
            @Override
            public void onTick(long l) {
                int time = 0;
                time += (l / 1000);
                gameTime.setText(time + " секунд");
            }


            /**
             * в методе onFinish пишем логику, которая будет выполняться после завершения таймера
             */
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFinish() {

                /**
                 * buttonsEnabled() в нашем случае деактивирует кнопки
                 * txtView.setEnabled() в нашем случае активирует то красное окошко, где выводится названия чисел
                 *
                 * поиграйте со сменой значений
                 */
                buttonsEnabled(false);
                txtView.setEnabled(true);

                txtView.setTextSize(30);
                txtView.setText("Вы набрали " + count + " очков\nНажмите START для повторной игры");

                // делаем из мухы слона, активити будет вызываться как диалог
                LayoutInflater showRecord = LayoutInflater.from(Game.this);
                View showRecordActivity = showRecord.inflate(R.layout.show_record, null);

                TextView tvTitle = showRecordActivity.findViewById(R.id.tvTitle);
                TextView tvTextRecord = showRecordActivity.findViewById(R.id.tvShowTextRecord);

                ImageView iv = showRecordActivity.findViewById(R.id.ivShowRecord);


                /**
                 * считывание файла myRecord, куда мы сохранили значение рекорда
                 */
                recordPreferences = getSharedPreferences("myRecord", MODE_PRIVATE);
                String res = recordPreferences.getString(KEY, "");

                // если файл был пустой, то...
                if (res.isEmpty()) {
                    // записываем туда наш рекорд
                    writeRecord();

                    // создаем диалог и...
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Game.this);

                    tvTitle.setText("Ваш первый рекорд!!!");
                    tvTextRecord.setText("Ваш рекорд: " + count);

                    dialog.setView(showRecordActivity);

                    // устанавливаем картинку showwin
                    iv.setImageResource(R.drawable.showwin);

                    // выдаем звук
                    sound(R.raw.soundwin);

                    // отображаем диалог
                    dialog.show();
                }

                // иначе, если файл не пустой и значение из файла меньше текущего рекорда, т.е. установлен
                // новый рекорд
                else if (Integer.parseInt(res) < count) {
                    // записываем туда наш рекорд
                    writeRecord();

                    // создаем диалог
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Game.this);

                    tvTitle.setText("Новый рекорд!!!");
                    tvTextRecord.setText("Ваш рекорд: " + count);

                    dialog.setView(showRecordActivity);

                    // устанавливаем картинку showwin
                    iv.setImageResource(R.drawable.showwin);

                    // выдаем звук
                    /**
                     * здесь логическая ошибка
                     * найдите и исправьте
                     */
                    sound(R.raw.soundfail);

                    dialog.show();
                } else {
                    // создаем диалог
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Game.this);

                    dialog.setView(showRecordActivity);

                    tvTitle.setText("Рекорд не побит");
                    tvTextRecord.setText("Ваш текущий рекорд: " + count);

                    // устанавливаем картинку fail
                    iv.setImageResource(R.drawable.fail);

                    // выдаем звук
                    sound(R.raw.soundfail);

                    dialog.show();
                }

                txtView.setOnClickListener(view -> {

                    txtView.setTextSize(64);

                    // меняем кнопки
                    setRandomShuffleValueButton();

                    // выдаем новое слово
                    randomSelectWord();

                    // заново запускаем весь игровой процесс
                    gameLogic();

                    // сбрасываем счетчик
                    count = 0;

                    gameCount.setText("Счет: " + count);
                });
            }
        }.start();
    }

    // рандомная выборка слов из массива строк
    private void randomSelectWord() {
        txtView.setText(WORD[(int) (Math.random() * WORD.length)]);
    }

    /**
     * в методе clickNumbers() реализован рандом без повторений (Random shuffle)
     * новые рандомные значения передаем кнопкам
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setRandomShuffleValueButton() {
        Random random = new Random();
        List<Object> randomNumbers = random.ints(1, 10)
                .distinct().limit(9).boxed().collect(Collectors.toList());

        button_n1.setText(String.valueOf(randomNumbers.get(0)));
        button_n2.setText(String.valueOf(randomNumbers.get(1)));
        button_n3.setText(String.valueOf(randomNumbers.get(2)));
        button_n4.setText(String.valueOf(randomNumbers.get(3)));
        button_n5.setText(String.valueOf(randomNumbers.get(4)));
        button_n6.setText(String.valueOf(randomNumbers.get(5)));
        button_n7.setText(String.valueOf(randomNumbers.get(6)));
        button_n8.setText(String.valueOf(randomNumbers.get(7)));
        button_n9.setText(String.valueOf(randomNumbers.get(8)));
    }

    public void backButton(View view) {
        /**
         * здесь сами реализуйте логику перехода с Game на MainActivity
         */
    }
}