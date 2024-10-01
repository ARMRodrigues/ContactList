package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityContactBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_VIEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class ContactActivity : AppCompatActivity() {
    private val acb: ActivityContactBinding by lazy {
        ActivityContactBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acb.root)

        setSupportActionBar(acb.toolbarInclude.toolbar)
        supportActionBar?.subtitle = "Contact Details"

        val receivedContact = intent.getParcelableExtra<Contact>(EXTRA_CONTACT)
        receivedContact?.let { received ->
            val viewContact = intent.getBooleanExtra(EXTRA_VIEW_CONTACT, false)
            with(acb) {
                if (viewContact) {
                    nameEditText.isEnabled = false
                    addressEditText.isEnabled = false
                    phoneEditText.isEnabled = false
                    emailEditText.isEnabled = false
                    saveButton.visibility = GONE
                }
                nameEditText.setText(received.name)
                addressEditText.setText(received.address)
                phoneEditText.setText(received.phone)
                emailEditText.setText(received.email)
            }
        }

        with(acb) {
            saveButton.setOnClickListener{
                val contact = Contact (
                    id = receivedContact?.id?:hashCode(),
                    name = nameEditText.text.toString(),
                    address = addressEditText.text.toString(),
                    phone = phoneEditText.text.toString(),
                    email = emailEditText.text.toString()
                )

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_CONTACT, contact)

                setResult(RESULT_OK, resultIntent)

                finish()
            }
        }
    }
}