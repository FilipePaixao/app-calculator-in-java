package com.example.calculator_ifpr;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private enum Op { ADD, SUB, MUL, DIV, NONE }

    private StringBuilder current  = new StringBuilder("0");
    private double       result    = 0;
    private Op           pendingOp = Op.NONE;

    private TextView tvInput;

    private final Map<Integer, Op> opById = new HashMap<Integer, Op>() {{
        put(R.id.btnAdd,      Op.ADD);
        put(R.id.btnSubtract, Op.SUB);
        put(R.id.btnMultiply, Op.MUL);
        put(R.id.btnDivide,   Op.DIV);
    }};

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInput = findViewById(R.id.tvInput);

        int[] allIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot,
                R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide,
                R.id.btnEqual, R.id.btnClear, R.id.btnPlusMinus, R.id.btnPercent
        };
        for (int id : allIds) findViewById(id).setOnClickListener(this);

        refreshDisplay();
    }

    @Override public void onClick(View v) {
        int id = v.getId();

        if (opById.containsKey(id)) {
            setOperator(opById.get(id));
            return;
        }

        switch (id) {
            case R.id.btnEqual:      compute();      break;
            case R.id.btnClear:      clear();        break;
            case R.id.btnPlusMinus:  toggleSign();   break;
            case R.id.btnPercent:    percent();      break;
            default:                 append(((Button) v).getText().toString());
        }
    }

    private void append(String input) {

        if (input.equals(".")) {
            if (current.indexOf(".") != -1) return;
        } else if (current.toString().equals("0")) {
            current.setLength(0);
        }
        current.append(input);
        refreshDisplay();
    }

    private void setOperator(Op op) {
        compute();
        pendingOp = op;
        current.setLength(0);
        current.append("0");
    }

    private void compute() {
        double value = Double.parseDouble(current.toString());

        switch (pendingOp) {
            case ADD: result += value;                 break;
            case SUB: result -= value;                 break;
            case MUL: result *= value;                 break;
            case DIV: result = value == 0 ? 0 : result / value; break;
            case NONE: result = value;                 break;
        }
        pendingOp = Op.NONE;
        current.replace(0, current.length(), format(result));
        refreshDisplay();
    }

    private void clear() {
        current.replace(0, current.length(), "0");
        result    = 0;
        pendingOp = Op.NONE;
        refreshDisplay();
    }

    private void toggleSign() {
        if (current.toString().equals("0")) return;
        if (current.charAt(0) == '-') current.deleteCharAt(0);
        else                           current.insert(0, '-');
        refreshDisplay();
    }

    private void percent() {
        double val = Double.parseDouble(current.toString()) / 100.0;
        current.replace(0, current.length(), format(val));
        refreshDisplay();
    }
    private void refreshDisplay() { tvInput.setText(current); }

    private String format(double d) {
        return (d == (long) d) ? String.valueOf((long) d) : String.valueOf(d);
    }
}
