package ec.edu.monster.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.monster.R;
import ec.edu.monster.controller.ConversionController;

public class ConversionActivity extends AppCompatActivity {

    Spinner categorySpinner, fromSpinner, toSpinner;
    EditText valueField;
    Button convertButton;
    TextView resultLabel;
    ConversionController controller = new ConversionController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);

        categorySpinner = findViewById(R.id.categorySpinner);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        valueField = findViewById(R.id.valueField);
        convertButton = findViewById(R.id.convertButton);
        resultLabel = findViewById(R.id.resultLabel);

        setupSpinners();
        convertButton.setOnClickListener(v -> convertUnit());
    }

    private void setupSpinners() {
        String[] categories = {"Seleccione", "mass", "length", "temperature"};
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(catAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String cat = categories[pos];
                fromSpinner.setAdapter(new ArrayAdapter<>(ConversionActivity.this,
                        android.R.layout.simple_spinner_item,
                        getUnits(cat)));
                toSpinner.setAdapter(new ArrayAdapter<>(ConversionActivity.this,
                        android.R.layout.simple_spinner_item,
                        getUnits(cat)));
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private String[] getUnits(String category) {
        switch (category) {
            case "mass": return new String[]{"kg", "g", "lb", "oz"};
            case "length": return new String[]{"m", "cm", "km", "in"};
            case "temperature": return new String[]{"C", "F", "K", "R"};
            default: return new String[]{"Seleccione una categoría"};
        }
    }

    private void convertUnit() {
        String cat = categorySpinner.getSelectedItem().toString();
        String from = fromSpinner.getSelectedItem().toString();
        String to = toSpinner.getSelectedItem().toString();
        String valStr = valueField.getText().toString();

        if (cat.equals("Seleccione") || from.equals("Seleccione una categoría") || to.equals("Seleccione una categoría") || valStr.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double val;
        try {
            val = Double.parseDouble(valStr);
        } catch (Exception e) {
            Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        new AsyncTask<Double, Void, Double>() {
            protected Double doInBackground(Double... params) {
                try {
                    return controller.convert(cat, from, to, val);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            protected void onPostExecute(Double result) {
                if (result != null) {
                    resultLabel.setText(String.format("Resultado: %.6f %s", result, to));
                } else {
                    resultLabel.setText("Error al conectar, usando modo local...");
                    double localResult = controller.convert(cat, from, to, val);
                    resultLabel.append(String.format("\nResultado local: %.6f %s", localResult, to));
                }
            }
        }.execute(val);
    }
}
