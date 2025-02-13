import java.util.*;
import java.time.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

class ContactDB {
    public static Contact[] contactArray = new Contact[0];

    public void addContacts(Contact contact) {
        Contact[] tempContactArray = new Contact[contactArray.length + 1];
        for (int i = 0; i < contactArray.length; i++) {
            tempContactArray[i] = contactArray[i];
        }
        tempContactArray[contactArray.length] = contact; // Add the new contact
        contactArray = tempContactArray;
    }

    public void deleteContacts(Contact contact) {
        int index = searchContacts(contact);
        deleteContacts(index);
    }

    public void deleteContacts(int index) {
        if (index >= 0 && index < contactArray.length) {
            for (int i = index; i < contactArray.length - 1; i++) {
                contactArray[i] = contactArray[i + 1];
            }

            Contact[] tempContactArray = new Contact[contactArray.length - 1];
            for (int i = 0; i < tempContactArray.length; i++) {
                tempContactArray[i] = contactArray[i];
            }
            contactArray = tempContactArray;
        }
    }

    public void replaceContact(int index, Contact contact) {
        if (index >= 0 && index < contactArray.length) {
            contactArray[index] = contact;
        }
    }

    public int searchContacts(String id) {
        for (int i = 0; i < contactArray.length; i++) {
            if (id.equalsIgnoreCase(contactArray[i].getID())) {
                return i;
            }
        }
        return -1;
    }

    public Contact[] getAllContacts() {
        Contact[] tempContactArray = new Contact[contactArray.length];
        for (int i = 0; i < contactArray.length; i++) {
            tempContactArray[i] = contactArray[i];
        }
        return tempContactArray;
    }

    public int searchContacts(Contact contact) {
        for (int i = 0; i < contactArray.length; i++) {
            if (contact.equals(contactArray[i])) {
                return i;
            }
        }
        return -1;
    }

    public int size() {
        return contactArray.length;
    }

    public Contact get(int index) {
        if (index >= 0 && index < contactArray.length) {
            return contactArray[index];
        }
        return null;
    }
}

class AddContactForm extends JFrame {
    private JLabel lblId;
    private JLabel lblName;
    private JLabel lblPhoneNumber;
    private JLabel lblCompanyName;
    private JLabel lblSalary;
    private JLabel lblBirthDay;

    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtPhoneNumber;
    private JTextField txtCompanyName;
    private JTextField txtSalary;
    private JTextField txtBirthDay;

    private JButton btnAdd;
    private JButton btnCancel;

    private ContactDB contactDB;

    AddContactForm(ContactDB contactDB) {
        this.contactDB = contactDB;
        setSize(400, 300);
        setTitle("Add Contact Form");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Add New Contact");
        titleLabel.setFont(new Font("", 1, 28));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add("North", titleLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("Add");
        btnAdd.setFont(new Font("", 1, 18));
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String id = txtId.getText();
                String name = txtName.getText();
                String phoneNumber = txtPhoneNumber.getText();
                String companyName = txtCompanyName.getText();
                double salary = Double.parseDouble(txtSalary.getText());
                String birthday = txtBirthDay.getText();

                Contact c1 = new Contact(id, name, phoneNumber, companyName, salary, birthday);
                contactDB.addContacts(c1);
                JOptionPane.showMessageDialog(null, "Added Success.");
                generateContactID();
                txtName.setText("");
                txtPhoneNumber.setText("");
                txtCompanyName.setText("");
                txtSalary.setText("");
                txtBirthDay.setText("");
            }
        });
        buttonPanel.add(btnAdd);
        btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("", 1, 18));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dispose();
            }
        });
        buttonPanel.add(btnCancel);

        add("South", buttonPanel);

        JPanel labelPanel = new JPanel(new GridLayout(6, 1));

        lblId = new JLabel("Id");
        lblId.setFont(new Font("", 1, 18));
        labelPanel.add(lblId);

        lblName = new JLabel("Name");
        lblName.setFont(new Font("", 1, 18));
        labelPanel.add(lblName);

        lblPhoneNumber = new JLabel("Phone Number");
        lblPhoneNumber.setFont(new Font("", 1, 18));
        labelPanel.add(lblPhoneNumber);

        lblCompanyName = new JLabel("Company Name");
        lblCompanyName.setFont(new Font("", 1, 18));
        labelPanel.add(lblCompanyName);

        lblSalary = new JLabel("Salary");
        lblSalary.setFont(new Font("", 1, 18));
        labelPanel.add(lblSalary);

        lblBirthDay = new JLabel("BirthDay");
        lblBirthDay.setFont(new Font("", 1, 18));
        labelPanel.add(lblBirthDay);

        add("West", labelPanel);

        JPanel textPanel = new JPanel(new GridLayout(6, 1));
        txtId = new JTextField(6);
        txtId.setEditable(false);
        txtId.setFont(new Font("", 1, 18));
        JPanel idTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        idTextPanel.add(txtId);
        textPanel.add(idTextPanel);

        txtName = new JTextField(10);
        txtName.setFont(new Font("", 1, 18));
        txtName.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent evt) {
                if (txtName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Empty contact name");
                    txtName.requestFocus();
                }
            }

            public void focusGained(FocusEvent arg0) {
                txtName.selectAll();
            }
        });
        JPanel nameTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameTextPanel.add(txtName);
        textPanel.add(nameTextPanel);

        txtPhoneNumber = new JTextField(10);
        txtPhoneNumber.setFont(new Font("", 1, 18));
        txtPhoneNumber.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent evt) {
                if (txtPhoneNumber.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Phone number cannot be empty");
                    txtPhoneNumber.requestFocus();
                } else if (!isValidPhoneNumber(txtPhoneNumber.getText())) {
                    JOptionPane.showMessageDialog(null, "Invalid phone number");
                    txtPhoneNumber.requestFocus();
                }
            }

            public void focusGained(FocusEvent arg0) {
                txtPhoneNumber.selectAll();
            }
        });
        JPanel phoneNumberTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phoneNumberTextPanel.add(txtPhoneNumber);
        textPanel.add(phoneNumberTextPanel);

        txtCompanyName = new JTextField(10);
        txtCompanyName.setFont(new Font("", 1, 18));
        JPanel companyNameTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        companyNameTextPanel.add(txtCompanyName);
        textPanel.add(companyNameTextPanel);

        txtSalary = new JTextField(10);
        txtSalary.setFont(new Font("", 1, 18));
        JPanel salaryTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        salaryTextPanel.add(txtSalary);
        textPanel.add(salaryTextPanel);

        txtBirthDay = new JTextField(10);
        txtBirthDay.setFont(new Font("", 1, 18));
        JPanel birthdayTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        birthdayTextPanel.add(txtBirthDay);
        textPanel.add(birthdayTextPanel);

        add("Center", textPanel);

        generateContactID();
        txtName.requestFocus();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{10}"); // Example: 10-digit phone number
    }

    private void generateContactID() {
        if (contactDB.size() == 0) {
            txtId.setText("B0001");
        } else {
            Contact lastContact = contactDB.get(contactDB.size() - 1);
            String lastId = lastContact.getID();
            int lastIdNumber = Integer.parseInt(lastId.substring(1));
            String newId = String.format("B%04d", (lastIdNumber + 1));
            txtId.setText(newId);
        }
    }
}

class SearchContactsForm extends JFrame {
    private JLabel lblId;
    private JLabel lblName;
    private JLabel lblPhoneNumber;
    private JLabel lblCompanyName;
    private JLabel lblSalary;
    private JLabel lblBirthDay;

    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtPhoneNumber;
    private JTextField txtCompanyName;
    private JTextField txtSalary;
    private JTextField txtBirthDay;

    private JButton btnSearch;
    private JButton btnCancel;

    private ContactDB contactDB;

    SearchContactsForm(ContactDB contactDB) {
        this.contactDB = contactDB;
        setSize(400, 300);
        setTitle("Search Contact Form");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Search Contact");
        titleLabel.setFont(new Font("", 1, 28));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add("North", titleLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("", 1, 18));
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String id = txtId.getText();
                int index = contactDB.searchContacts(id);
                if (index != -1) {
                    Contact c1 = contactDB.get(index);
                    txtName.setText(c1.getName());
                    txtPhoneNumber.setText(c1.getPhoneNumber());
                    txtCompanyName.setText(c1.getCompanyName());
                    txtSalary.setText(String.valueOf(c1.getSalary()));
                    txtBirthDay.setText(c1.getBday());
                } else {
                    JOptionPane.showMessageDialog(null, id + " is not exists...");
                }
            }
        });
        buttonPanel.add(btnSearch);
        btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("", 1, 18));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dispose();
            }
        });
        buttonPanel.add(btnCancel);

        add("South", buttonPanel);

        JPanel labelPanel = new JPanel(new GridLayout(6, 1));

        lblId = new JLabel("Id");
        lblId.setFont(new Font("", 1, 18));
        labelPanel.add(lblId);

        lblName = new JLabel("Name");
        lblName.setFont(new Font("", 1, 18));
        labelPanel.add(lblName);

        lblPhoneNumber = new JLabel("Phone Number");
        lblPhoneNumber.setFont(new Font("", 1, 18));
        labelPanel.add(lblPhoneNumber);

        lblCompanyName = new JLabel("Company Name");
        lblCompanyName.setFont(new Font("", 1, 18));
        labelPanel.add(lblCompanyName);

        lblSalary = new JLabel("Salary");
        lblSalary.setFont(new Font("", 1, 18));
        labelPanel.add(lblSalary);

        lblBirthDay = new JLabel("BirthDay");
        lblBirthDay.setFont(new Font("", 1, 18));
        labelPanel.add(lblBirthDay);

        add("West", labelPanel);

        JPanel textPanel = new JPanel(new GridLayout(6, 1));
        txtId = new JTextField(6);
        txtId.setFont(new Font("", 1, 18));
        txtId.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String id = txtId.getText();
                int index = contactDB.searchContacts(id);
                if (index != -1) {
                    Contact c1 = contactDB.get(index);
                    txtName.setText(c1.getName());
                    txtPhoneNumber.setText(c1.getPhoneNumber());
                    txtCompanyName.setText(c1.getCompanyName());
                    txtSalary.setText(String.valueOf(c1.getSalary()));
                    txtBirthDay.setText(c1.getBday());
                } else {
                    JOptionPane.showMessageDialog(null, id + " is not exists...");
                }
            }
        });
        JPanel idTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        idTextPanel.add(txtId);
        textPanel.add(idTextPanel);

        txtName = new JTextField(10);
        txtName.setFont(new Font("", 1, 18));
        JPanel nameTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameTextPanel.add(txtName);
        textPanel.add(nameTextPanel);

        txtPhoneNumber = new JTextField(10);
        txtPhoneNumber.setFont(new Font("", 1, 18));
        JPanel phoneNumberTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phoneNumberTextPanel.add(txtPhoneNumber);
        textPanel.add(phoneNumberTextPanel);

        txtCompanyName = new JTextField(10);
        txtCompanyName.setFont(new Font("", 1, 18));
        JPanel companyNameTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        companyNameTextPanel.add(txtCompanyName);
        textPanel.add(companyNameTextPanel);

        txtSalary = new JTextField(10);
        txtSalary.setFont(new Font("", 1, 18));
        JPanel salaryTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        salaryTextPanel.add(txtSalary);
        textPanel.add(salaryTextPanel);

        txtBirthDay = new JTextField(10);
        txtBirthDay.setFont(new Font("", 1, 18));
        JPanel birthdayTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        birthdayTextPanel.add(txtBirthDay);
        textPanel.add(birthdayTextPanel);

        add("Center", textPanel);
        txtId.requestFocus();
    }
}

class DeleteContactsForm extends JFrame {
    private JLabel lblId;
    private JLabel lblName;
    private JLabel lblPhoneNumber;
    private JLabel lblCompanyName;
    private JLabel lblSalary;
    private JLabel lblBirthDay;

    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtPhoneNumber;
    private JTextField txtCompanyName;
    private JTextField txtSalary;
    private JTextField txtBirthDay;

    private JButton btnDelete;
    private JButton btnCancel;

    private ContactDB contactDB;

    DeleteContactsForm(ContactDB contactDB) {
        this.contactDB = contactDB;
        setSize(400, 300);
        setTitle("Delete Contact Form");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Delete Contact");
        titleLabel.setFont(new Font("", 1, 28));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add("North", titleLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnDelete = new JButton("Delete");
        btnDelete.setFont(new Font("", 1, 18));
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String id = txtId.getText();
                int index = contactDB.searchContacts(id);
                if (index != -1) {
                    contactDB.deleteContacts(index);
                    JOptionPane.showMessageDialog(null, "Contact deleted successfully.");
                    txtId.setText("");
                    txtName.setText("");
                    txtPhoneNumber.setText("");
                    txtCompanyName.setText("");
                    txtSalary.setText("");
                    txtBirthDay.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, id + " is not exists...");
                }
            }
        });
        buttonPanel.add(btnDelete);
        btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("", 1, 18));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dispose();
            }
        });
        buttonPanel.add(btnCancel);

        add("South", buttonPanel);

        JPanel labelPanel = new JPanel(new GridLayout(6, 1));

        lblId = new JLabel("Id");
        lblId.setFont(new Font("", 1, 18));
        labelPanel.add(lblId);

        lblName = new JLabel("Name");
        lblName.setFont(new Font("", 1, 18));
        labelPanel.add(lblName);

        lblPhoneNumber = new JLabel("Phone Number");
        lblPhoneNumber.setFont(new Font("", 1, 18));
        labelPanel.add(lblPhoneNumber);

        lblCompanyName = new JLabel("Company Name");
        lblCompanyName.setFont(new Font("", 1, 18));
        labelPanel.add(lblCompanyName);

        lblSalary = new JLabel("Salary");
        lblSalary.setFont(new Font("", 1, 18));
        labelPanel.add(lblSalary);

        lblBirthDay = new JLabel("BirthDay");
        lblBirthDay.setFont(new Font("", 1, 18));
        labelPanel.add(lblBirthDay);

        add("West", labelPanel);

        JPanel textPanel = new JPanel