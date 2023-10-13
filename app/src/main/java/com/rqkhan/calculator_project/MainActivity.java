package com.rqkhan.calculator_project;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MainActivity extends AppCompatActivity {

     EditText input;
     EditText output;

     String Onwork = "";
     String formula = "";
     String tempformula = "";

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gettingText();
    }
    // getting and showing text
    // giris ve cikistaki edittextleri id ile erismeye saglamistir
    private void gettingText(){
        input = findViewById(R.id.input);
        output = findViewById(R.id.output);

        //this method will block the pop up of the keyboard when it is Onclicked
        // edittextte uzerinde bastgimizde kilavya cikmasini engeleyen methodtur
        input.setShowSoftInputOnFocus(false);
        output.setShowSoftInputOnFocus(false);

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            /* when its clicket if the source file(string.xml) (caculate somthing) is equal to the string which shown on the screen
              by onclick it will be cleared with adding new string(input.setText("");)
             */
            /*normalda uygulamanın acıldıgında input kısmınde Calculat somthing yazıyor asagıdakı method
            ustunde basıldıgınde ve calculate something oldugunu fark edince siler yanı boş giriş saglar (input.setText("");)
             */
            public void onClick(View v) {
                if (getString(R.string.show).equals(input.getText().toString())) {
                    input.setText("");
                }
            }
        });
    }

     // adding valu and showing on the input
    /* her botunda asagıdakı methodu cağırıp girilen değere göre işlem yapıp ekrarna gosterir
     */
    private  void setWork(String givenVlaue){
        // IF the user click on the C it will remove last digit of the entered value
        // C botununa basıldıgında Onwırktaki girilmiş son rakamı siler
        if (givenVlaue =="C"){
            // boş olup olmadığı kontrol eder
            if(Onwork.isEmpty() || Onwork.length()-1<=1){
                input.setText(Onwork);
            }
                // removing the last digit
            // son basamagı siler
                Onwork = Onwork.substring(0, Onwork.length()-1);
                // showing the lated version
            // guncel haline göster
                input.setText(Onwork);

        }else{
            // C'ye esıt olmadıgı halde normal basılan sayıya yan yana ekler
        Onwork += givenVlaue;
        input.setText(Onwork);

    }}

    public void equalBTM(View view){
        checkforpow(); // ust alma funksounun cagırlması
        // here i used an javascrip library
        /* Aşağıdaki kutuphaneye kulanmadan once dosyalar kısmından calculator_project/gardle Scrıpts/build.gradle
             dependencies kısmında  implementation 'io.apisense:rhino-android:1.0' implement ettim*/

        /*Bu kod, Java'da Rhino adlı bir JavaScript motorunu kullanarak bir betik motorunu başlatır.
         ScriptEngineManager sınıfı, Java uygulamalarında betik motorlarını elde etmek
          ve yönetmek için kullanılır. burada girilen dort işeme gore işlem yapmasına sağlar
         */
        Double result = null;
        // ScriptEngin kutuphanesinde bir nesne oluşturdum
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");

        // if someone enter just string (+,-....) when its catch it show on the screen (yanlish giris)
        try {
            result = (double)engine.eval(formula);
        }catch (ScriptException e){
            Toast.makeText(this , "yanlis giris", Toast.LENGTH_SHORT).show();
        }
        if(result != null){
            // resultun boş olmadığı dueumde son halını ekrana gosterir
            output.setText(String.valueOf(result.doubleValue()));
        }
    }


 // clicking on AC everything will be cleard by adding empty value
    // AC basıldıgıda ekranı temizler yanı hepsını yeni değerine boş strıng atar
    public void cleanAllBTM(View view){
        input.setText("");
        Onwork= "";
        output.setText("");
        leftparethes = true;
    }

    private void checkforpow(){
        // this arraylist search for finding the power symbule"^" and save it into arraylis
        // asagıdakı arralıst ust alma sembolunua "^" bulur ve arrayde kayet eder sonrakı adımlarda kulanması için
        ArrayList<Integer> indexofpow = new ArrayList<>();
        for(int i = 0; i < Onwork.length(); i++){
            if(Onwork.charAt(i) == '^')
                indexofpow.add(i);
            }
        formula = Onwork;
        tempformula = Onwork;
        // change to the power symbol to the formula or what it should do
        // uste sembolu "^" funksiyon hala çevirir
        for(Integer index: indexofpow){
            changetoformula(index);
        }
        formula = tempformula;
    }
// 4^512 +1 in this case we are going to finde to 4 and 5 which is numbr left and right
    // asagıdakı methoda 4^512 +1 oreneğinde sağ ve sol sayısı olan 4 ve 5 buluruz ve gereken işlem yaptırırız
    private void changetoformula(Integer index){
        String numleft = "";
        String numRight = "";
        // this for loop while try to finde the number which is on the right (ex: 4^512 trying to find 512..)
        // aşağıdaki for dongusu ust sembolun sağnda kalan sayıyı bulur ve numRıghtta kayıt eder(ex: 4^512 trying to find 512..)
        for(int i = index +1; i<Onwork.length(); i++){
            // gırılen charakterın sayı oldugunde calışır
            if (isdigit(Onwork.charAt(i)))
            {
                numRight = numRight + Onwork.charAt(i);
            }else
                break;

        }

        // this for loop while try to finde the number which is on the left (ex: 4^512 trying to find 4)
        // aşağıdaki for dongusu ust sembolun solunda kalan sayıyı bulur ve numleftta kayıt eder(ex: 4^512 trying to find 4)
        for(int i = index -1; i>= 0; i--){
            // gırılen charakterın sayı oldugunde calışır
            if (isdigit(Onwork.charAt(i))){
                numleft = numleft + Onwork.charAt(i);
            }else
                break;
        }

    String orignal = numleft + "^" + numRight; // 4^5
    String Counted = "Math.pow("+numleft+","+numRight+")";// Math.pow(4,5)
    tempformula = tempformula.replace(orignal, Counted);

    }
// this method check wheather the character is numeric or not
    // girilen charkter rakam veya nokta olup olmadıgını kontrol eder
    private  boolean isdigit(char c){
        if( (c <= '9' && c >= '0') || c == '.'){
            return true;
        }

        return false;
    }
    // aşağıdakı methodelerde setWork() methodunu çağırıp gereken işlemi yapar
    public void zeroBTM(View view){
        setWork("0");
    }

    public void oneBTM(View view){
        setWork("1");
    }

    public void twoBTM(View view){
        setWork("2");
    }

    public void treeBTM(View view){
        setWork("3");
    }

    public void fourBTM(View view){
        setWork("4");
    }

    public void fiveBTM(View view){
        setWork("5");
    }

    public void sixBTM(View view){
        setWork("6");
    }

    public void sevenBTM(View view){
        setWork("7");
    }

    public void eightBTM(View view){
        setWork("8");
    }

    public void nineBTM(View view){
        setWork("9");
    }

    public void multiplyBTM(View view){
        setWork("*");
    }

    public void minusBTM(View view){
        setWork("-");
    }

    public void plusBTM(View view){
        setWork("+");
    }

    public void dividBTM(View view){
        setWork("/");
    }

    public void removeLastDigitBtn(View view){
        setWork("C");
    }

    public void dotBTM(View view){
        setWork(".");
    }
    public void powerBTM(View view){
        setWork("^");
    }

    boolean leftparethes = true;
    // aşagıdaki method sağ parantis olduğunu kontol edip ıkıncı kez () bastığmızde sol parantez kulnamamızı izin verir
    public void prantheseBTM(View view){
        if (leftparethes){
            setWork("(");
            leftparethes = false;
        }else {
            setWork(")");
            leftparethes = true;

        }

    }

}