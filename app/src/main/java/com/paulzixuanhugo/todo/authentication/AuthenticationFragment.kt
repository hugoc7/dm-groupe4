package com.paulzixuanhugo.todo.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.paulzixuanhugo.todo.R


class AuthenticationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_authentication, container, false)
        val signUpButton = view.findViewById<Button>(R.id.signup_button)
        val loginButton = view.findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_authenticationFragment_to_loginFragment)
        }
        signUpButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_authenticationFragment_to_signupFragment)
        }
        return view
    }

}