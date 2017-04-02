package com.example.hmv.chatbot;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIContext;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonElement;
import ai.api.model.AIRequest;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button listenButton;
    private TextView resultTextView;
    private EditText textV;
    private AIService aiService;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listenButton = (Button) findViewById(R.id.listenButton);
        textV = (EditText) findViewById(R.id.editText);
        final TextView resultTextView1 = (TextView) findViewById(R.id.textView);
        resultTextView = (TextView) findViewById(R.id.textView1);

        final AIConfiguration config = new AIConfiguration("a540f456608a459db3de8fd6458b9759",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        final AIDataService aiDataService = new AIDataService(config);

        final AIRequest aiRequest = new AIRequest();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        listenButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String input =textV.getText().toString().trim();
                aiRequest.setQuery(input);
                resultTextView1.setText(input);
                //aiService.setListener(this);
                new AsyncTask<AIRequest, Void, AIResponse>() {
                    @Override
                    protected AIResponse doInBackground(AIRequest... requests) {
                        final AIRequest request = requests[0];
                        try {
                            final AIResponse response = aiDataService.request(aiRequest);
                            return response;
                        } catch (AIServiceException e) {
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(AIResponse aiResponse) {
                        if (aiResponse != null) {
                            Result result = aiResponse.getResult();

                            // Get parameters
                            String parameterString = "";
                            String resultString = "";

                            if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                                for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                                    parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                                }

                            }
                            if (result.getFulfillment() != null) {
                                resultString += "(" + result.getFulfillment().getSpeech() + ") ";
                            }
                            // Show results in TextView.
                            resultTextView.setText("Query:" + result.getResolvedQuery() +
                                    "\nAction: " + result.getAction() +
                                    "\nParameters: " + parameterString +
                                    "\nReply:" + resultString);
                        }
                    }
                }.execute(aiRequest);
            }
        });

    }
}
//    public void onResult(final AIResponse response) {
//        Result result = response.getResult();
//
//        // Get parameters
//        String parameterString = "";
//        String resultString = "";
//
//        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
//            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
//                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
//
//
//            }
//
//        }
//        if (result.getFulfillment() != null) {
//            resultString += "(" + result.getFulfillment().getSpeech() + ") ";
//        }
//        // Show results in TextView.
//        resultTextView.setText("Query:" + result.getResolvedQuery() +
//                "\nAction: " + result.getAction() +
//                "\nParameters: " + parameterString +
//                "\nReply:" + resultString);
//    }
//
//    @Override
//    public void onError(final AIError error) {
//        resultTextView.setText(error.toString());
//    }
//
//    @Override
//    public void onListeningStarted() {
//    }
//
//    @Override
//    public void onListeningCanceled() {
//    }
//
//    @Override
//    public void onListeningFinished() {
//    }
//
//    @Override
//    public void onAudioLevel(final float level) {
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.example.zainahmeds.chatbot/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.example.zainahmeds.chatbot/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }
//}
