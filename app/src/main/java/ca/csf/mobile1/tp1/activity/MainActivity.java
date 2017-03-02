package ca.csf.mobile1.tp1.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import ca.csf.mobile1.tp1.R;
import ca.csf.mobile1.tp1.chemical.compound.ChemicalCompound;
import ca.csf.mobile1.tp1.chemical.compound.ChemicalCompoundFactory;
import ca.csf.mobile1.tp1.chemical.compound.EmptyFormulaException;
import ca.csf.mobile1.tp1.chemical.compound.EmptyParenthesisException;
import ca.csf.mobile1.tp1.chemical.compound.IllegalCharacterException;
import ca.csf.mobile1.tp1.chemical.compound.IllegalClosingParenthesisException;
import ca.csf.mobile1.tp1.chemical.compound.MisplacedExponentException;
import ca.csf.mobile1.tp1.chemical.compound.MissingClosingParenthesisException;
import ca.csf.mobile1.tp1.chemical.compound.UnknownChemicalElementException;
import ca.csf.mobile1.tp1.chemical.element.ChemicalElement;
import ca.csf.mobile1.tp1.chemical.element.ChemicalElementRepository;

/**
 * Class MainActivity est le contrôleur de l'application
 * pour une architecture MVC
 */
public class MainActivity extends AppCompatActivity {

    private View rootView;
    private EditText inputEditText;
    private TextView outputTextView;
    private ChemicalCompoundFactory model;
    private ChemicalElementRepository chemicalElementRepository;
    private String formula;
    private String molecularWeightText;

    /**
     * La méthode onCreate génère et prépare les attributs privés
     * de l'application.
     * @param savedInstanceState conserve les valeurs nécessaires
     *                           à la réinitialisation de l'application selon son dernier état
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView = findViewById(R.id.rootView);
        inputEditText = (EditText) findViewById(R.id.inputEditText);
        outputTextView = (TextView) findViewById(R.id.outputTextView);

        chemicalElementRepository = new ChemicalElementRepository();
        try{
            loadChemicalElementRepository();
        }
        catch (Exception e)
        {
            MainActivity.this.finish();
        }

        model = new ChemicalCompoundFactory(chemicalElementRepository);
        if(savedInstanceState != null)
        {
            inputEditText.setText(savedInstanceState.getString("currentInputText"));
            outputTextView.setText(savedInstanceState.getString("currentOutPutText"));

        }
        else
        {
            formula = "";
            molecularWeightText = "";
        }

    }

    /**
     * La méthode lance la procédure qui permet de calculer le poids d'un composé chimiques
     * @param view la view de l'application pour l'architecture MVC
     */
    public void onComputeButtonClicked(View view) {

        try {

            double molarWeigth = 0;
            inputEditText = (EditText) findViewById(R.id.inputEditText);
            formula = inputEditText.getText().toString();

            ChemicalCompound compound= model.createFromString(formula);
            molarWeigth = compound.getWeight();

            molecularWeightText = getResources().getString(R.string.text_output,molarWeigth);
            outputTextView = (TextView) findViewById(R.id.outputTextView);
            outputTextView.setText(molecularWeightText);

            }
        catch (EmptyParenthesisException e)
        {
            Snackbar.make(rootView, R.string.text_empty_parenthesis, Snackbar.LENGTH_LONG).show();
        }
        catch (IllegalCharacterException e)
        {

            Snackbar.make(rootView, getString(R.string.text_illegal_character,e.getCharacter()), Snackbar.LENGTH_LONG).show();
        }
        catch (IllegalClosingParenthesisException e)
        {

            Snackbar.make(rootView, R.string.text_illegal_closing_parenthesis, Snackbar.LENGTH_LONG).show();
        }
        catch (MisplacedExponentException e)
        {

            Snackbar.make(rootView, R.string.text_misplaced_exponent, Snackbar.LENGTH_LONG).show();
        }
        catch (EmptyFormulaException e)
        {

            Snackbar.make(rootView, R.string.text_empty_formula, Snackbar.LENGTH_LONG).show();
        }
        catch (UnknownChemicalElementException e)
        {
            Snackbar.make(rootView, getString(R.string.text_unknown_chemical_element,e.getElement()), Snackbar.LENGTH_LONG).show();
        }
        catch (MissingClosingParenthesisException e)
        {
            Snackbar.make(rootView, R.string.text_missing_closing_parenthesis, Snackbar.LENGTH_LONG).show();
        }


    }

    /**
     * Méthode effectuant le chargement des masse molaires des éléments chimiques du tableau périodique
     * @throws Exception correspondant à un échec du chargement des masse molaires
     */
     private void loadChemicalElementRepository() throws Exception
     {


         try {

             String language = Locale.getDefault().getLanguage();
             InputStream inputStream = getResources().openRawResource(R.raw.chemical_elements);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader);

             String currentLine;

             while((currentLine = reader.readLine()) != null)
             {
                 String[] currentlineSplitted = currentLine.split(",");
                 chemicalElementRepository.add(new ChemicalElement(currentlineSplitted[0],currentlineSplitted[1],Integer.parseInt(currentlineSplitted[2]),Double.parseDouble(currentlineSplitted[3])));
             }
             reader.close();
             inputStreamReader.close();
             inputStream.close();
         }
         catch (Exception err)
         {
             Snackbar.make(rootView, err.getMessage(), Snackbar.LENGTH_LONG).show();
             throw err;
         }

     }

    /**
     * Réinitialise l'application dans son dernier état lorsque celle-ci est dormante.
     * @param savedInstanceState conserve les valeurs nécessaires
     *                           à la réinitialisation de l'application selon son dernier état
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        inputEditText = (EditText) findViewById(R.id.inputEditText);
        outputTextView = (TextView) findViewById(R.id.outputTextView);
        inputEditText.setText(savedInstanceState.getString("currentInputText"));
        outputTextView.setText(savedInstanceState.getString("currentOutPutText"));
    }

    /**
     * Méthode permettant de préserver l'état de l'application pour une réinitialisation futur.
     * @param outState conserve les valeurs nécessaires
     *                           à la réinitialisation de l'application selon son état actuel
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        inputEditText = (EditText) findViewById(R.id.inputEditText);
        outputTextView = (TextView) findViewById(R.id.outputTextView);
        String inputTextToKeep = inputEditText.getText().toString();
        String outputTextToKeep = outputTextView.getText().toString();
        outState.putString("currentInputText",inputTextToKeep);
        outState.putString("currentOutPutText",outputTextToKeep);
    }


}
