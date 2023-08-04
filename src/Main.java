import java.io.IOException;
import java.util.*;

// Операнд
class Operand {
    String valueS; // Текстовое значение
    int valueI; // Целочисленное значение
    boolean isRoman; // Признак - римские цифры
    boolean isBadValue; // Признак - цифры нераспознаны

     Operand(String value) {
        valueS = value;

        if( valueS.matches("[0123456789]+?") ) { // Если попадает под формат арабских цифр
            valueI = Integer.parseInt (valueS);
            isRoman = false;
            if( valueI >= 1 && valueI <= 10) {
                isBadValue = false;
            }
            else {
                isBadValue = true;
            }
        }
        else if(valueS.matches("[IVX]+?") ){ // Если попадает под формат римских  цифр
            switch (valueS) {
                case "I":
                    valueI = 1; isBadValue = false; break;
                case "II":
                    valueI = 2; isBadValue = false; break;
                case "III":
                    valueI = 3; isBadValue = false; break;
                case "IV":
                    valueI = 4; isBadValue = false; break;
                case "V":
                    valueI = 5; isBadValue = false; break;
                case "VI":
                    valueI = 6; isBadValue = false; break;
                case "VII":
                    valueI = 7; isBadValue = false; break;
                case "VIII":
                    valueI = 8; isBadValue = false; break;
                case "IX":
                    valueI = 9; isBadValue = false; break;
                case "X":
                    valueI = 10; isBadValue = false; break;
                default:
                    valueI = 0; isBadValue = true;
            }
            isRoman = true;
        }
        else { // Если не распознали - включаем признак плохого операнда
            valueI = 0;
            isBadValue = true;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        String input = "";

        // Прочитать выражение
        input = readInput();

        // Обработать выражение
        if (input != "" )
            System.out.println( input + " = " + calc( input) );
        else
            System.out.println( "Пример вызова калькулятора: 2+3" );
    }

    // Чтение входного потока
    static String readInput() {
        char charReaded;
        String tmp = "";

        // Читаем входные символы, формируем из них строку, пока не встретим Enter. Enter в строку не добавляем
        do {
            try {
                charReaded = (char)System.in.read();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(charReaded != 10) tmp = tmp + charReaded;
        } while (charReaded != 10);

        return tmp;
    }

    // Перевод арабских чисел в римские
    static String intToRoman(int number) { // Источник - https://ru.wikipedia.org/wiki/Римские_цифры
        NavigableMap<Integer, String> units;

        NavigableMap<Integer, String> initMap = new TreeMap<>();
        initMap.put(1000, "M");
        initMap.put(900, "CM");
        initMap.put(500, "D");
        initMap.put(400, "CD");
        initMap.put(100, "C");
        initMap.put(90, "XC");
        initMap.put(50, "L");
        initMap.put(40, "XL");
        initMap.put(10, "X");
        initMap.put(9, "IX");
        initMap.put(5, "V");
        initMap.put(4, "IV");
        initMap.put(1, "I");
        units = Collections.unmodifiableNavigableMap(initMap);

        if (number >= 4000 || number <= 0)
            return null;
        StringBuilder result = new StringBuilder();
        for(Integer key : units.descendingKeySet()) {
            while (number >= key) {
                number -= key;
                result.append(units.get(key));
            }
        }
        return result.toString();
    }

    // Вычисление выражения
    public static String calc(String input) {
        String[] inputParsed = input.split ("[+\\*/-]"); // Парсим входное выражение по символу оператора на 2 части
        Operand[] operands = new Operand[2];
        String operator = "", outputS = "";
        int outputI;
        int i;

        if(inputParsed.length !=2) {
            throw new RuntimeException( "Неправильный формат выражения" );
        }

        // Выделяем оператор из выражения
        operator = input.substring( inputParsed[0].length(), input.length()-inputParsed[1].length() );

        // Инициализируем операнды
        for(i=0;i< inputParsed.length;i++) {
            operands[i] = new Operand(inputParsed[i]);
        }

        if ( operands[0].isBadValue | operands[1].isBadValue) {
            throw new RuntimeException( "Формат операндов не распознан" );
        }

        if ( operands[0].isRoman ^ operands[1].isRoman) {
            throw new RuntimeException( "Операнды в разном формате" );
        }

        // Вычисление
        switch ( operator ) {
            case "+":
                outputI = operands[0].valueI + operands[1].valueI; break;
            case "-":
                outputI = operands[0].valueI - operands[1].valueI; break;
            case "*":
                outputI = operands[0].valueI * operands[1].valueI; break;
            case "/":
                outputI = operands[0].valueI / operands[1].valueI; break;
            default:
                throw new RuntimeException( "Неизвестный оператор" );
        }

        if( operands[0].isRoman) { // Преобразуем в римские цифры, если на входе были римские
            if(outputI<1) {
                throw new RuntimeException( "При вычислении с римскими цифрами получено отрицательное число или 0" );
            }
            else {
                outputS = intToRoman( outputI);
            }
        }
        else {
            outputS = String.valueOf(outputI);
        }

        return outputS;
    }
}