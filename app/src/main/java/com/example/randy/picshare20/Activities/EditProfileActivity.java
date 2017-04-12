package com.example.randy.picshare20.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randy.picshare20.Model.Consts;
import com.example.randy.picshare20.Model.User;
import com.example.randy.picshare20.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import static com.example.randy.picshare20.Model.Consts.DATABASE_PATH_UPLOADS_EDUCATION;
import static com.example.randy.picshare20.Model.Consts.DATABASE_PATH_UPLOADS_ETHNICITY;
import static com.example.randy.picshare20.Model.Consts.DATABASE_PATH_UPLOADS_GENDER;
import static com.example.randy.picshare20.Model.Consts.DATABASE_PATH_UPLOADS_PARTNER;
import static com.example.randy.picshare20.Model.Consts.DATABASE_PATH_UPLOADS_PROFILE;
import static com.example.randy.picshare20.Model.Consts.DATABASE_PATH_UPLOADS_RELIGION;
import static com.example.randy.picshare20.Model.Consts.DATABASE_PATH_UPLOADS_USER;

public class EditProfileActivity extends AppCompatActivity {
    private static final String SPINNER_DEFAULT = "Choose One";

    ArrayList<String> educationList, genderList, ethnicityList, religionList;
    Spinner educationSpinner, genderSpinner, ethnicitySpinner, religionSpinner, educationPartnerSpinner, genderPartnerSpinner, ethnicityPartnerSpinner, religionPartnerSpinner;
    EditText usernameBox, emailBox, nameBox, heightFeetBox, heightInchesBox, weightBox, zipBox;
    EditText partnerheightFeet, partnerHeightInches, partnerWeight;
    String username, email, name, zipcode, heightFeet, heightInches, weight, partnerHeightFeetString, partnerHeightInchesString, partnerWeightString;
    String education, gender, ethnicity, religion, partnerEducation, partnerGender, partnerEthnicity, partnerReligion;

    private DatabaseReference mDatabase;
    private String uploadId;
    private String currentUsername, currentEmail, currentName, currentZipcode, currentheight, currentWeight, currentEducation, currentGender, currentEthnicity, currentReligion;
    private String currentPartnerHeight, currentPartnerWeight, currentPartnerEducation, currentPartnerGender, currentPartnerEthnicity, currentPartnerReligion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Spinner for user and partner
        educationSpinner = (Spinner)findViewById(R.id.education_spinner);
        genderSpinner = (Spinner)findViewById(R.id.gender_spinner);
        ethnicitySpinner = (Spinner)findViewById(R.id.ethnicity_spinner);
        religionSpinner = (Spinner)findViewById(R.id.religion_spinner);

        educationPartnerSpinner = (Spinner)findViewById(R.id.education_partner_spinner);
        genderPartnerSpinner = (Spinner)findViewById(R.id.gender_partner_spinner);
        ethnicityPartnerSpinner = (Spinner)findViewById(R.id.ethnicity_partner_spinner);
        religionPartnerSpinner = (Spinner)findViewById(R.id.religion_partner_spinner);

        //Edit Text box for user and partner
        usernameBox = (EditText)findViewById(R.id.profile_get_username);
        emailBox = (EditText)findViewById(R.id.profile_get_email);
        nameBox = (EditText)findViewById(R.id.profile_get_name);
        heightFeetBox = (EditText)findViewById(R.id.profile_get_height_feet);
        heightInchesBox = (EditText)findViewById(R.id.profile_get_height_inches);
        weightBox = (EditText)findViewById(R.id.profile_get_weight);
        zipBox = (EditText)findViewById(R.id.profile_get_zip);

        partnerheightFeet = (EditText)findViewById(R.id.profile_get_partner_height_feet);
        partnerHeightInches = (EditText)findViewById(R.id.profile_get_partner_height_inches);
        partnerWeight = (EditText)findViewById(R.id.profile_get_partner_weight);

        addEducationToList(educationSpinner, null);
        addEducationToList(educationPartnerSpinner, null);
        addGenderToList(genderSpinner, null);
        addGenderToList(genderPartnerSpinner, null);
        addEthnicityToList(ethnicitySpinner, null);
        addEthnicityToList(ethnicityPartnerSpinner, null);
        addReligionToList(religionSpinner, null);
        addReligionToList(religionPartnerSpinner, null);



        uploadId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference(Consts.DATABASE_PATH_UPLOADS_USER + "/" + uploadId + "/" + Consts.DATABASE_PATH_UPLOADS_PROFILE);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);


                if(FirebaseAuth.getInstance().getCurrentUser().getEmail() != null){
                    currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    emailBox.setText(currentEmail);
                }
                if(user.getUsername() != null){
                    currentUsername = user.getUsername();
                    usernameBox.setText(currentUsername);
                }

                dataSnapshot.getChildren();
                Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();

                if(data.get(Consts.DATABASE_PATH_UPLOADS_NAME) != null){
                    currentName = data.get(Consts.DATABASE_PATH_UPLOADS_NAME).toString();
                    nameBox.setText(currentName);
                }
                if(data.get(Consts.DATABASE_PATH_UPLOADS_ZIP) != null){
                    currentZipcode = data.get(Consts.DATABASE_PATH_UPLOADS_ZIP).toString();
                    zipBox.setText(currentZipcode);
                }
                if(data.get(Consts.DATABASE_PATH_UPLOADS_HEIGHT) != null){
                    currentheight = data.get(Consts.DATABASE_PATH_UPLOADS_HEIGHT).toString();
                    String delims = "[ '\"]+";
                    String[] tokens = currentheight.split(delims);
                    if(tokens[0] != null){
                        heightFeetBox.setText(tokens[0]);
                    }
                    if(tokens[1] != null){
                        heightInchesBox.setText(tokens[1]);
                    }
                }
                if(data.get(Consts.DATABASE_PATH_UPLOADS_WEIGHT) != null){
                    currentWeight = data.get(Consts.DATABASE_PATH_UPLOADS_WEIGHT).toString();
                    weightBox.setText(currentWeight);
                }
                if(data.get(Consts.DATABASE_PATH_UPLOADS_EDUCATION) != null){
                    currentEducation = data.get(Consts.DATABASE_PATH_UPLOADS_EDUCATION).toString();
                    addEducationToList(educationSpinner, currentEducation);
                }
                if(data.get(Consts.DATABASE_PATH_UPLOADS_GENDER) != null){
                    currentGender = data.get(Consts.DATABASE_PATH_UPLOADS_GENDER).toString();
                    addGenderToList(genderSpinner, currentGender);
                }
                if(data.get(Consts.DATABASE_PATH_UPLOADS_ETHNICITY) != null){
                    currentEthnicity = data.get(Consts.DATABASE_PATH_UPLOADS_ETHNICITY).toString();
                    addEthnicityToList(ethnicitySpinner, currentEthnicity);
                }
                if(data.get(Consts.DATABASE_PATH_UPLOADS_RELIGION) != null){
                    currentReligion = data.get(Consts.DATABASE_PATH_UPLOADS_RELIGION).toString();
                    addReligionToList(religionSpinner, currentReligion);
                }

                Map<String, Object> partnerData = (Map<String, Object>) data.get(Consts.DATABASE_PATH_UPLOADS_PARTNER);

                if(partnerData.get(Consts.DATABASE_PATH_UPLOADS_HEIGHT) != null){
                    currentPartnerHeight = partnerData.get(Consts.DATABASE_PATH_UPLOADS_HEIGHT).toString();
                    String delims = "[ '\"]+";
                    String[] tokens = currentPartnerHeight.split(delims);
                    if(tokens[0] != null){
                        partnerheightFeet.setText(tokens[0]);
                    }
                    if(tokens[1] != null){
                        partnerHeightInches.setText(tokens[1]);
                    }

                }
                if(partnerData.get(Consts.DATABASE_PATH_UPLOADS_WEIGHT) != null){
                    currentPartnerWeight = partnerData.get(Consts.DATABASE_PATH_UPLOADS_WEIGHT).toString();
                    partnerWeight.setText(currentPartnerWeight);
                }
                if(partnerData.get(Consts.DATABASE_PATH_UPLOADS_EDUCATION) != null){
                    currentPartnerEducation = partnerData.get(Consts.DATABASE_PATH_UPLOADS_EDUCATION).toString();
                    addEducationToList(educationPartnerSpinner, currentPartnerEducation);
                }
                if(partnerData.get(Consts.DATABASE_PATH_UPLOADS_GENDER) != null){
                    currentPartnerGender = partnerData.get(Consts.DATABASE_PATH_UPLOADS_GENDER).toString();
                    addGenderToList(genderPartnerSpinner, currentPartnerGender);
                }
                if(partnerData.get(Consts.DATABASE_PATH_UPLOADS_ETHNICITY) != null){
                    currentPartnerEthnicity = partnerData.get(Consts.DATABASE_PATH_UPLOADS_ETHNICITY).toString();
                    addEthnicityToList(ethnicityPartnerSpinner, currentPartnerEthnicity);
                }
                if(partnerData.get(Consts.DATABASE_PATH_UPLOADS_RELIGION) != null){
                    currentPartnerReligion = partnerData.get(Consts.DATABASE_PATH_UPLOADS_RELIGION).toString();
                    addReligionToList(religionPartnerSpinner, currentPartnerReligion);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.add));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameBox.getText().toString();
                email = emailBox.getText().toString();
                name = nameBox.getText().toString();
                zipcode = zipBox.getText().toString();
                heightFeet = heightFeetBox.getText().toString();
                heightInches = heightInchesBox.getText().toString();
                weight = weightBox.getText().toString();
                partnerHeightFeetString = partnerheightFeet.getText().toString();
                partnerHeightInchesString = partnerHeightInches.getText().toString();
                partnerWeightString = partnerWeight.getText().toString();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if(username != null && username.length() > 0){
                    User aUser = new User(username);
                    FirebaseDatabase.getInstance().getReference(Consts.DATABASE_PATH_UPLOADS_USER).child(userId).child(DATABASE_PATH_UPLOADS_PROFILE).setValue(aUser);
                }
                if(email != null && email.length() > 0){
                    //update email needs to be in its own update area
                }
                if(name != null && name.length() > 0){
                    FirebaseDatabase.getInstance().getReference(Consts.DATABASE_PATH_UPLOADS_USER).child(userId).child(DATABASE_PATH_UPLOADS_PROFILE).child(Consts.DATABASE_PATH_UPLOADS_NAME).setValue(name);
                }
                if(zipcode != null && zipcode.length() > 0){
                    FirebaseDatabase.getInstance().getReference(Consts.DATABASE_PATH_UPLOADS_USER).child(userId).child(DATABASE_PATH_UPLOADS_PROFILE).child(Consts.DATABASE_PATH_UPLOADS_ZIP).setValue(zipcode);

                }
                if(heightFeet != null && heightInches != null && heightFeet.length() > 0 && heightInches.length() > 0){
                    String height = heightFeet + "' " + heightInches + "\"";
                    FirebaseDatabase.getInstance().getReference(Consts.DATABASE_PATH_UPLOADS_USER).child(userId).child(DATABASE_PATH_UPLOADS_PROFILE).child(Consts.DATABASE_PATH_UPLOADS_HEIGHT).setValue(height);
                }
                if(weight != null && weight.length() > 0){
                    FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USER).child(userId).child(DATABASE_PATH_UPLOADS_PROFILE).child(Consts.DATABASE_PATH_UPLOADS_WEIGHT).setValue(weight);
                }
                if (partnerHeightFeetString != null && partnerHeightInchesString != null && partnerHeightFeetString.length() > 0 && partnerHeightInchesString.length() > 0) {
                    String partnerHeight = partnerHeightFeetString + "' " + partnerHeightInchesString + "\"";
                    FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USER).child(userId).child(DATABASE_PATH_UPLOADS_PROFILE).child(DATABASE_PATH_UPLOADS_PARTNER).child(Consts.DATABASE_PATH_UPLOADS_HEIGHT).setValue(partnerHeight);
                }
                if (partnerWeightString != null && partnerWeightString.length() > 0) {
                    FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USER).child(userId).child(DATABASE_PATH_UPLOADS_PROFILE).child(DATABASE_PATH_UPLOADS_PARTNER).child(Consts.DATABASE_PATH_UPLOADS_WEIGHT).setValue(partnerWeightString);
                }

                education = educationSpinner.getSelectedItem().toString();
                gender = genderSpinner.getSelectedItem().toString();
                ethnicity = ethnicitySpinner.getSelectedItem().toString();
                religion = religionSpinner.getSelectedItem().toString();
                partnerEducation = educationPartnerSpinner.getSelectedItem().toString();
                partnerGender = genderPartnerSpinner.getSelectedItem().toString();
                partnerEthnicity = ethnicityPartnerSpinner.getSelectedItem().toString();
                partnerReligion = religionPartnerSpinner.getSelectedItem().toString();

                if(!education.equals(SPINNER_DEFAULT)){
                    FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USER).child(userId).child(DATABASE_PATH_UPLOADS_PROFILE).child(DATABASE_PATH_UPLOADS_EDUCATION).setValue(education);
                }
                if(!gender.equals(SPINNER_DEFAULT)){
                    FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USER).child(userId).child(DATABASE_PATH_UPLOADS_PROFILE).child(DATABASE_PATH_UPLOADS_GENDER).setValue(gender);
                }
                if(!ethnicity.equals(SPINNER_DEFAULT)){
                    FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USER).child(userId).child(DATABASE_PATH_UPLOADS_PROFILE).child(DATABASE_PATH_UPLOADS_ETHNICITY).setValue(ethnicity);
                }
                if(!religion.equals(SPINNER_DEFAULT)){
                    FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USER).child(userId).child(DATABASE_PATH_UPLOADS_PROFILE).child(DATABASE_PATH_UPLOADS_RELIGION).setValue(religion);
                }
                if(!partnerEducation.equals(SPINNER_DEFAULT)){
                    FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USER).child(userId).child(DATABASE_PATH_UPLOADS_PROFILE).child(DATABASE_PATH_UPLOADS_PARTNER).child(DATABASE_PATH_UPLOADS_EDUCATION).setValue(partnerEducation);
                }
                if(!partnerGender.equals(SPINNER_DEFAULT)){
                    FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USER).child(userId).child(DATABASE_PATH_UPLOADS_PROFILE).child(DATABASE_PATH_UPLOADS_PARTNER).child(DATABASE_PATH_UPLOADS_GENDER).setValue(partnerGender);
                }
                if(!partnerEthnicity.equals(SPINNER_DEFAULT)){
                    FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USER).child(userId).child(DATABASE_PATH_UPLOADS_PROFILE).child(DATABASE_PATH_UPLOADS_PARTNER).child(DATABASE_PATH_UPLOADS_ETHNICITY).setValue(partnerEthnicity);
                }
                if(!partnerReligion.equals(SPINNER_DEFAULT)){
                    FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USER).child(userId).child(DATABASE_PATH_UPLOADS_PROFILE).child(DATABASE_PATH_UPLOADS_PARTNER).child(DATABASE_PATH_UPLOADS_RELIGION).setValue(partnerReligion);
                }

                Toast.makeText(EditProfileActivity.this, "Profile Updated",
                        Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    private void addEducationToList(Spinner spinner, String selectedValue){
        educationList = new ArrayList<>();
        educationList.add(SPINNER_DEFAULT);
        educationList.add("No education");
        educationList.add("High School/GED");
        educationList.add("Some College");
        educationList.add("Associate Degree");
        educationList.add("Bachelor Degree");
        educationList.add("Masters Degree");
        educationList.add("PHD");
        educationList.add("Other");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, educationList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrayAdapter);
        if (selectedValue != null) {
            int spinnerPosition = arrayAdapter.getPosition(selectedValue);
            spinner.setSelection(spinnerPosition);
        }
    }

    private void addGenderToList(Spinner spinner, String selectedValue){
        genderList = new ArrayList<>();
        genderList.add(SPINNER_DEFAULT);
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Non-binary");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genderList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrayAdapter);
        if (selectedValue != null) {
            int spinnerPosition = arrayAdapter.getPosition(selectedValue);
            spinner.setSelection(spinnerPosition);
        }
    }

    private void addEthnicityToList(Spinner spinner, String selectedValue){
        ethnicityList = new ArrayList<>();
        ethnicityList.add(SPINNER_DEFAULT);
        ethnicityList.add("Asian");
        ethnicityList.add("Black");
        ethnicityList.add("Hispanic/Latin");
        ethnicityList.add("Indian");
        ethnicityList.add("Middle Eastern");
        ethnicityList.add("Native American");
        ethnicityList.add("Pacific Islander");
        ethnicityList.add("White");
        ethnicityList.add("Other");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ethnicityList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrayAdapter);
        if (selectedValue != null) {
            int spinnerPosition = arrayAdapter.getPosition(selectedValue);
            spinner.setSelection(spinnerPosition);
        }

    }

    private void addReligionToList(Spinner spinner, String selectedValue){
        religionList = new ArrayList<>();
        religionList.add(SPINNER_DEFAULT);
        religionList.add("Agnosticism");
        religionList.add("Atheism");
        religionList.add("Christianity");
        religionList.add("Judaism");
        religionList.add("Catholicism");
        religionList.add("Islam");
        religionList.add("Hinduism");
        religionList.add("Buddhism");
        religionList.add("Sikh");
        religionList.add("Other");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, religionList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrayAdapter);
        if (selectedValue != null) {
            int spinnerPosition = arrayAdapter.getPosition(selectedValue);
            spinner.setSelection(spinnerPosition);
        }
    }
}
