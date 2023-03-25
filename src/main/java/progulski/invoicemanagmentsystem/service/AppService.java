package progulski.invoicemanagmentsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import progulski.invoicemanagmentsystem.domain.Invoice;
import progulski.invoicemanagmentsystem.domain.Item;
import progulski.invoicemanagmentsystem.repository.ItemRepository;
import progulski.invoicemanagmentsystem.domain.Role;
import progulski.invoicemanagmentsystem.domain.User;
import progulski.invoicemanagmentsystem.repository.InvoiceRepository;
import progulski.invoicemanagmentsystem.repository.RoleRepository;
import progulski.invoicemanagmentsystem.repository.UserRepository;

import javax.persistence.EntityExistsException;
import java.rmi.NoSuchObjectException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AppService {
    private final InvoiceRepository invoiceRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public Role addRole(String role){
        String[] possibleRoles = {"ROLE_CUSTOMER","ROLE_EMPLOYEE"}; //lista wszystkich zaimplementowanych w aplikacji ról
        if(!Arrays.asList(possibleRoles).contains(role)){
            throw new IllegalStateException("Not implemented role");
        }
        Role foundRole = roleRepository.findByName(role);
        if(foundRole==null){ //jeśli zaimplementowanej roli nie ma jeszcze w bazie danych
            Role newRole = new Role();
            newRole.setName(role);
            roleRepository.save(newRole);
            foundRole = roleRepository.findByName(role);
        }
        return foundRole;
    }

    public User addCustomer(User user){
        return addUser(user,"ROLE_CUSTOMER");
    }
    public User addEmployee(User user){
        return addUser(user,"ROLE_EMPLOYEE");
    }
    public User addUser(User user,String role){
        if(!validateUsernameAndPassword(user.getUsername(), user.getPassword())){
            throw new IllegalStateException("Forbidden sign");
        }
        if(checkIfUsernameIsTaken(user.getUsername())){
            throw new EntityExistsException("Login in use");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Collection<Role> defaultRole = new ArrayList<>();
        defaultRole.add(addRole(role));
        user.setRoles(defaultRole);
        return userRepository.save(user);
    }
    public Boolean validateUsernameAndPassword(String username, String password){
        if(username.contains(" ")){return false;}
        if(password.contains(" ")){return false;}
        if(username.equals("")){return false;}
        if(password.equals("")){return false;}
        return true;
    }
    public Boolean checkIfUsernameIsTaken(String username){
        return !userRepository.findByUsername(username).equals(Optional.empty());
    }

    public void addRoleToUser(String username, String role){
        User user = userRepository.findByUsername(username).get();
        Collection<Role> roles = user.getRoles();
        roles.add(addRole(role));
        user.setRoles(roles);
        userRepository.save(user);
    }

    public Invoice addInvoice(Invoice invoice) {
        invoice.getItems().stream().forEach(item -> item.setTotalPrice());
        invoice.setTotalPrice();
        final Invoice savedInvoice = invoiceRepository.save(invoice);
        invoice.getItems().stream().forEach(item -> item.setInvoice(savedInvoice));
        invoice.getItems().stream().forEach(item -> addItem(item));
        return savedInvoice;
    }
    public void addItem(Item item){
        itemRepository.save(item);
    }

    public Invoice getInvoice(int id) throws NoSuchObjectException {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if(invoice.isPresent()){
            return invoice.get();
        }
        else{
            throw new NoSuchObjectException("Invoice doesn't exist");
        }
    }

//    public Set<Invoice> getInvoicesFromCurrentMonth() {
//        int currentMonth = LocalDate.now().getMonth().getValue();
//        return invoiceRepository.getInvoicesFromCurrentMonth();
//    }
}
