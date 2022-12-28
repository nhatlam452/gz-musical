package com.example.duantotnghiep;

import android.app.Activity;
import android.os.Looper;
import android.util.Log;

import com.example.duantotnghiep.Activities.LoginActivity;
import com.example.duantotnghiep.Contract.UserContract;
import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Presenter.UserPresenter;
import com.facebook.login.Login;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UserTest {
    UserContract.View view;
    UserContract.Model model;

    @Before
    public void setUp() {
        // Mock the Looper class
        view = Mockito.mock(UserContract.View.class);
        model = Mockito.mock(UserContract.Model.class);
    }

    @Test
    public void testOnLogin_Success() {
        // create mock objects
        UserContract.View view = Mockito.mock(UserContract.View.class);
        UserContract.Model model = Mockito.mock(UserContract.Model.class);

        // create presenter and call onLogin method
        UserPresenter presenter = new UserPresenter(view, model);
        String phone = "0909916020";
        String password = "Nhatlam452@";
        presenter.onLogin(phone, password);

        // verify that getLogin is called with the correct arguments
        Mockito.verify(model).getLogin(presenter, phone, password);
    }

    @Test
    public void testOnLogin_Fail() {
        // create mock objects
        UserContract.View view = Mockito.mock(UserContract.View.class);

        // create presenter and call onLogin method
        UserPresenter presenter = new UserPresenter(view, null);
        String phone = "12345";
        String password = "123";
        presenter.onLogin(phone, password);

        // verify that showError is called with the correct error message
        Mockito.verify(view).onFail("Invalid phone number or password");
    }

//    @Test
//    public void testOnRegister() {
//        // Create a mock User object with valid values for all required fields
//        User user = new User();
//        user.setPhoneNumber("1234567890");
//        user.setPassword("123456");
//        user.setSalutation("Mr.");
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setDob("01/01/1970");
//        user.setAddress("123 Main St");
//        user.setCity("New York");
//        user.setDistrict("NY");
//        user.setWard("1");
//
//        // Create a mock Activity object
//        Activity activity = Mockito.mock(Activity.class);
//
//        // Create a mock UserContract.Model.OnFinishedListener object
//        UserContract.Model.OnFinishedListener onFinishedListener = Mockito.mock(UserContract.Model.OnFinishedListener.class);
//
//        // Create a mock PhoneAuthCredential object
//        PhoneAuthCredential credential = Mockito.mock(PhoneAuthCredential.class);
//
//        // Create a mock FirebaseAuth object
//        FirebaseAuth mAuth = Mockito.mock(FirebaseAuth.class);
//
//        // Create a mock Task object with a successful result
//        Task<AuthResult> task = Mockito.mock(Task.class);
//        Mockito.when(task.isSuccessful()).thenReturn(true);
//
//        // Set up the mock FirebaseAuth object to return the mock Task object when signInWithCredential is called
//        Mockito.when(mAuth.signInWithCredential(credential)).thenReturn(task);
//
//        // Set up the mock Activity object to return the mock FirebaseAuth object when getInstance is called
//
//        // Create a mock UserPresenter object
//        UserPresenter presenter = Mockito.mock(UserPresenter.class);
//
//        // Set up the mock UserPresenter object to call the onRegister method when signInWithPhoneAuthCredential is called
//        Mockito.doCallRealMethod().when(presenter).signInWithPhoneAuthCredential(credential, user, onFinishedListener, activity);
//
//        // Call the onRegister method on the mock UserPresenter object
//        presenter.onRegister(user, "123456", "123456", activity);
//
//        // Verify that the signInWithPhoneAuthCredential method was called with the correct arguments
//        Mockito.verify(presenter).signInWithPhoneAuthCredential(credential, user, onFinishedListener, activity);
//
//        // Verify that the getRegister method of the model object was called with the correct arguments
//        Mockito.verify(model).getRegister(onFinishedListener, user);
//    }

    @Test
    public void testOnSocialRegister() {
        // Create a mock User object
        User user = new User();

        // Create a mock Activity object
        Activity activity = Mockito.mock(Activity.class);

        // Create a mock UserPresenter object
        UserPresenter presenter =  new UserPresenter(view,model);

        // Call the onSocialRegister method on the mock UserPresenter object
        presenter.onSocialRegister(user, activity);

        // Verify that the getSocialRegister method of the model object was called with the correct arguments
        Mockito.verify(model).getSocialRegister(presenter, user);
    }

    @Test
    public void testOnCheckExits() {
        // Create a mock phone number
        String phone = "1234567890";

        // Create a mock UserPresenter object
        UserPresenter presenter =  new UserPresenter(view,model);

        // Call the onCheckExits method on the mock UserPresenter object
        presenter.onCheckExits(phone);

        // Verify that the checkExitsUser method of the model object was called with the correct arguments
        Mockito.verify(model).checkExitsUser(presenter, phone);
    }

    @Test
    public void testOnUpdateInfo() {
        // Create mock values for the method arguments
        String avt = "avatar.jpg";
        String firstName = "John";
        String lastName = "Doe";
        String salutations = "Mr.";
        String userId = "123456";

        // Create a mock UserPresenter object
        UserPresenter presenter =  new UserPresenter(view,model);

        // Call the onUpdateInfo method on the mock UserPresenter object
        presenter.onUpdateInfo(avt, firstName, lastName, salutations, userId);

        // Verify that the updateUser method of the model object was called with the correct arguments
        Mockito.verify(model).updateUser(presenter, avt, firstName, lastName, salutations, userId);
    }

    @Test
    public void testOnChangePassword() {
        // Create mock values for the method arguments
        String phoneNumber = "1234567890";
        String newPassword = "123456";
        String oldPassword = "password";

        // Create a mock UserPresenter object
        UserPresenter presenter =  new UserPresenter(view,model);

        // Call the onChangePassword method on the mock UserPresenter object
        presenter.onChangePassword(phoneNumber, newPassword, oldPassword);

        // Verify that the changePassword method of the model object was called with the correct arguments
        Mockito.verify(model).changePassword(presenter, phoneNumber, newPassword, oldPassword);
    }

}
