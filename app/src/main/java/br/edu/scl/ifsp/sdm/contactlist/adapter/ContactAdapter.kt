package br.edu.scl.ifsp.sdm.contactlist.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.TileContactBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class ContactAdapter(context:Context, private val contactList: MutableList<Contact>):
    ArrayAdapter<Contact>(context, R.layout.tile_contact, contactList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contact = contactList[position]

        var contactTileView = convertView

        if (contactTileView == null) {
            val tileContactBinding = TileContactBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )
            contactTileView = tileContactBinding.root

            val tileContactHolder = TileContactHolder(tileContactBinding.nameTextView, tileContactBinding.emailTextView)
            contactTileView.tag = tileContactHolder
        }

        val holder = contactTileView.tag as TileContactHolder
        holder.nameTextView.text = contact.name
        holder.emailTextView.text = contact.email

        return contactTileView
    }

    private data class TileContactHolder(val nameTextView : TextView, val emailTextView : TextView)
}