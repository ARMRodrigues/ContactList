package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.adapter.ContactRecyclerViewAdapter
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_VIEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class MainActivity : AppCompatActivity(), OnContactClickListener {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data Source
    private val contactList : MutableList<Contact> = mutableListOf()

    // Adapter
    private val contactAdapter : ContactRecyclerViewAdapter by lazy {
        ContactRecyclerViewAdapter(contactList, this)
    }

    private lateinit var contactActivityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Contact List"

        contactActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val contact = result.data?.getParcelableExtra<Contact>(EXTRA_CONTACT)
                contact?.also { newOrEditContact ->
                    if (contactList.any{ it.id == newOrEditContact.id}) {
                        // Edit contact
                        val position = contactList.indexOfFirst { it.id == newOrEditContact.id }
                        contactList[position] = newOrEditContact
                        contactAdapter.notifyItemChanged(position)
                    }
                    else
                    {
                        contactList.add(contact)
                        contactAdapter.notifyItemInserted(contactList.lastIndex)
                    }
                }
            }
        }

        fillContacts()

        amb.contactListRecyclerView.adapter = contactAdapter
        amb.contactListRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.addContactMenuItem -> {
                contactActivityResultLauncher.launch(Intent(this, ContactActivity::class.java))
                true
            }
            else -> { false }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onContactClick(position: Int) {
        Intent(this, ContactActivity::class.java).apply {
            putExtra(EXTRA_CONTACT, contactList[position])
            putExtra(EXTRA_VIEW_CONTACT, true)
        }.also {
            startActivity(it)
        }
    }

    override fun onRemoveContactMenuItemClick(position: Int) {
        contactList.removeAt(position)
        contactAdapter.notifyItemRemoved(position)
        Toast.makeText(this, "Contact removed", Toast.LENGTH_LONG).show()
    }

    override fun onEditContactMenuItemCLick(position: Int) {
        contactActivityResultLauncher.launch(Intent(this, ContactActivity::class.java).apply {
            putExtra(EXTRA_CONTACT, contactList[position])
        })
    }

    private fun fillContacts() {
        for (i in 1 .. 3) {
            contactList.add(
                Contact(
                    i,
                    "Nome $i",
                    "Endereço $i",
                    "Telefone $i",
                    "$i@$i.com"
                )
            )
        }
    }
}