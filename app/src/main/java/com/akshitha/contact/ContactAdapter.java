package com.akshitha.contact;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> implements Filterable {

    private List<Contact> contacts;
    private List<Contact> contactsFiltered; // For search functionality
    private OnItemClickListener itemClickListener;

    public ContactAdapter(List<Contact> contacts, OnItemClickListener itemClickListener) {
        this.contacts = contacts;
        this.contactsFiltered = new ArrayList<>(contacts); // Initialize filtered list with all contacts
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactsFiltered.get(position); // Use filtered list for display
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return contactsFiltered.size(); // Use filtered list size
    }

    // Implement Filterable interface methods
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                List<Contact> filteredList = new ArrayList<>();

                if (filterPattern.isEmpty()) {
                    filteredList.addAll(contacts); // If search bar is empty, show all contacts
                } else {
                    for (Contact contact : contacts) {
                        // Search by name (you can modify this based on your search criteria)
                        if (contact.getFirstName().toLowerCase().contains(filterPattern) ||
                                contact.getLastName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(contact);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactsFiltered.clear();
                contactsFiltered.addAll((List<Contact>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView phoneNumberTextView;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);

            // Set click listener for the entire item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }

        public void bind(Contact contact) {
            // Concatenate first name and last name
            String fullName = contact.getFirstName() + " " + contact.getLastName();
            nameTextView.setText(fullName);
            phoneNumberTextView.setText(contact.getPhoneNumber());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}








////roomdata
//package com.akshitha.contact;
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.widget.Filter;
//        import android.widget.Filterable;
//        import android.widget.TextView;
//
//        import androidx.annotation.NonNull;
//        import androidx.recyclerview.widget.RecyclerView;
//
//        import java.util.ArrayList;
//        import java.util.List;
//
//public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> implements Filterable {
//
//    private List<ContactEntity> contactList;
//    private List<ContactEntity> contactListFull; // For filtering
//    private OnItemClickListener listener;
//
//    public interface OnItemClickListener {
//        void onItemClick(int position);
//    }
//
//    public ContactAdapter(List<ContactEntity> contactList, OnItemClickListener listener) {
//        this.contactList = contactList;
//        this.contactListFull = new ArrayList<>(contactList); // Copy for filtering
//        this.listener = listener;
//    }
//
//    @NonNull
//    @Override
//    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
//        return new ContactViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
//        ContactEntity currentContact = contactList.get(position);
//        holder.textViewName.setText(currentContact.getFirstName() + " " + currentContact.getLastName());
//        holder.textViewPhoneNumber.setText(currentContact.getPhoneNumber());
//    }
//
//    @Override
//    public int getItemCount() {
//        return contactList.size();
//    }
//
//    public class ContactViewHolder extends RecyclerView.ViewHolder {
//        public TextView textViewName;
//        public TextView textViewPhoneNumber;
//
//        public ContactViewHolder(View itemView) {
//            super(itemView);
//            textViewName = itemView.findViewById(R.id.nameTextView);
//            textViewPhoneNumber = itemView.findViewById(R.id.numberTextView);
//
//
//            itemView.setOnClickListener(v -> {
//                if (listener != null) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        listener.onItemClick(position);
//                    }
//                }
//            });
//        }
//    }
//
//    @Override
//    public Filter getFilter() {
//        return contactFilter;
//    }
//
//    private Filter contactFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<ContactEntity> filteredList = new ArrayList<>();
//
//            if (constraint == null || constraint.length() == 0) {
//                filteredList.addAll(contactListFull);
//            } else {
//                String filterPattern = constraint.toString().toLowerCase().trim();
//
//                for (ContactEntity contact : contactListFull) {
//                    if (contact.getFirstName().toLowerCase().contains(filterPattern)
//                            || contact.getLastName().toLowerCase().contains(filterPattern)
//                            || contact.getPhoneNumber().toLowerCase().contains(filterPattern)) {
//                        filteredList.add(contact);
//                    }
//                }
//            }
//
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            contactList.clear();
//            contactList.addAll((List<ContactEntity>) results.values);
//            notifyDataSetChanged();
//        }
//    };
//}
