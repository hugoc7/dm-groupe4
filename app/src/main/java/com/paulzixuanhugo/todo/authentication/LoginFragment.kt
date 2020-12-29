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
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.paulzixuanhugo.todo.MainActivity
import com.paulzixuanhugo.todo.R
import com.paulzixuanhugo.todo.SHARED_PREF_TOKEN_KEY
import com.paulzixuanhugo.todo.network.Api
import com.paulzixuanhugo.todo.tasklist.task.TaskActivity
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val emailInput = view.findViewById<EditText>(R.id.input_email)
        val passwordInput = view.findViewById<EditText>(R.id.input_password)
        val loginButton = view.findViewById<Button>(R.id.button_login)
        val userWebService = Api.INSTANCE.userService
        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            if (email != "" && password != "")
            {
                val loginForm = LoginForm(email, password)
                lifecycleScope.launch {
                    val res = userWebService.login(loginForm)
                    if(res.isSuccessful)
                    {
                        val token = res.body()?.token
                        PreferenceManager.getDefaultSharedPreferences(context).edit {
                            putString(SHARED_PREF_TOKEN_KEY, token)
                        }
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else
                        Toast.makeText(context, "Problème lors de la demande de login !", Toast.LENGTH_LONG).show()
                }
            }
            else
                Toast.makeText(context, "Vous devez remplir tous les champs !", Toast.LENGTH_LONG).show()
        }
    }
}