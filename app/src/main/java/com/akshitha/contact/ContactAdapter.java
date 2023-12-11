package com.akshitha.contact;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import java.util.List;
//
//public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
//
//    private List<Contact> contacts;
//    private OnItemClickListener itemClickListener;
//
//    public ContactAdapter(List<Contact> contacts, OnItemClickListener itemClickListener) {
//        this.contacts = contacts;
//        this.itemClickListener = itemClickListener;
//    }
//
//    @NonNull
//    @Override
//    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.contact_item, parent, false);
//        return new ContactViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
//        Contact contact = contacts.get(position);
//        holder.bind(contact);
//    }
//
//    @Override
//    public int getItemCount() {
//        return contacts.size();
//    }
//
//    public class ContactViewHolder extends RecyclerView.ViewHolder {
//        private TextView nameTextView;
//        private TextView phoneNumberTextView;
//
//        public ContactViewHolder(@NonNull View itemView) {
//            super(itemView);
//            nameTextView = itemView.findViewById(R.id.nameTextView);
//            phoneNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);
//
//            // Set click listener for the entire item view
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (itemClickListener != null) {
//                        itemClickListener.onItemClick(getAdapterPosition());
//                    }
//                }
//            });
//        }
//
//        public void bind(Contact contact) {
//            // Concatenate first name and last name
//            String fullName = contact.getFirstName() + " " + contact.getLastName();
//            nameTextView.setText(fullName);
//            phoneNumberTextView.setText(contact.getPhoneNumber());
//        }
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(int position);
//    }
//}




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
