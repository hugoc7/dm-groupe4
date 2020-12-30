package com.paulzixuanhugo.todo.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.paulzixuanhugo.todo.MainActivity
import com.paulzixuanhugo.todo.R
import com.paulzixuanhugo.todo.SHARED_PREF_TOKEN_KEY
import com.paulzixuanhugo.todo.network.Api
import kotlinx.coroutines.launch


class SignUpFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val emailInput = view.findViewById<EditText>(R.id.input_email)
        val passwordInput = view.findViewById<EditText>(R.id.input_password)
        val firstnameInput = view.findViewById<EditText>(R.id.input_firstname)
        val lastnameInput = view.findViewById<EditText>(R.id.input_lastname)
        val confirmPasswordInput = view.findViewById<EditText>(R.id.input_confirm_password)
        val signupButton = view.findViewById<Button>(R.id.button_signup)
        val userWebService = Api.INSTANCE.userService
        signupButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()
            val firstname = firstnameInput.text.toString()
            val lastname = lastnameInput.text.toString()

            if (email != "" && password != "" && confirmPassword != "" && firstname != "" && lastname != "")
            {
                if (password == confirmPassword) {
                    if (password.length > 5) {
                        val signupForm = SignUpForm(firstname, lastname, email, password, confirmPassword)
                        lifecycleScope.launch {
                            val res = userWebService.signup(signupForm)
                            if (res.isSuccessful) {
                                val token = res.body()?.token
                                PreferenceManager.getDefaultSharedPreferences(context).edit {
                                    putString(SHARED_PREF_TOKEN_KEY, token)
                                }
                                val intent = Intent(activity, MainActivity::class.java)
                                startActivity(intent)
                            } else
                                Toast.makeText(context, "Problème lors de la demande de sign up !", Toast.LENGTH_LONG).show()
                        }
                    }
                    else
                        Toast.makeText(context, "Les mots de passe doivent contenir au moins 6 charactères !", Toast.LENGTH_LONG).show()
                }
                else
                    Toast.makeText(context, "Les mots de passe doivent correspondre !", Toast.LENGTH_LONG).show()
            }
            else
                Toast.makeText(context, "Vous devez remplir tous les champs !", Toast.LENGTH_LONG).show()
        }
    }
}