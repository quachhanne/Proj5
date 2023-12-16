import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class BookBrowser1 extends JFrame {
    // TreeMap fields
    private TreeMap<String, Book> ISBNMap;
    private TreeMap<String, Book> authorMap;
    private TreeMap<Integer, Book> yearMap;
    private TreeMap<String, Book> originalTitleMap;
    private TreeMap<String, Book> titleMap;
    private TreeMap<Double, Book> ratingMap;
    // HashMap fields
    private HashMap<String, TreeMap<?, Book>> fieldTreeMaps;
    // Others
    private String currentField;
    private int currentIndex;

    private static final int X_LABEL = 20;
    private static final int X_TEXT = 115;
    private static final int WIDTH_TEXT = 370;
    private static final int HEIGHT_TEXT = 30;
    private static final int WIDTH_LABEL = 400;
    private static final int HEIGHT_LABEl = 50;
    private static final int HEIGHT_BUTTON = 35;
    private static final int WIDTH_BUTTON = 100;
    private static final int Y_BUTTON = 380;


    // GUI fields
    private JTextField isbnTextField, authorTextField, yearTextField;
    private JTextField originalTitleTextField, titleTextField, ratingTextField;

    private JLabel isbnLabel, authorLabel, yearLabel;
    private JLabel originalLabel, titleLabel, ratingLabel;

    private JButton next, previous, first,last;


    public BookBrowser1(String filename) throws FileNotFoundException {
        loadBookFromFile(filename);
        initializeGUI();
        fillTreeMaps();
    }


    private void loadBookFromFile(String filename) throws FileNotFoundException {
        initializeTreeMap();
        File file = new File(filename);
        Scanner sc = new Scanner(file);
        if (sc.hasNextLine()) {
            sc.nextLine();
        }
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] data = line.split("~");
            String ISBN = data[2];
            String author = data[3];
            int year = Integer.parseInt(data[4]);
            String originalTitle = data[5];
            String title = data[6];
            double rate = Double.parseDouble(data[7]);
            Book book = new Book(ISBN, author, year, originalTitle, title, rate);
            // Add the book to all TreeMap instances
            ISBNMap.put(ISBN, book);
            authorMap.put(author, book);
            yearMap.put(year,book);
            originalTitleMap.put(originalTitle, book);
            titleMap.put(title,book);
            ratingMap.put(rate,book);
        }
        sc.close();
    }

    private void initializeTreeMap(){
        authorMap = new TreeMap<>();
        ISBNMap = new TreeMap<>();
        yearMap = new TreeMap<>();
        originalTitleMap = new TreeMap<>();
        titleMap = new TreeMap<>();
        ratingMap = new TreeMap<>();
    }

    private void fillTreeMaps() {
        fieldTreeMaps = new HashMap<>();
        String[] fieldNames = {"author", "ISBN", "year", "originalTitle", "title", "rating"};
        // Create and add TreeMap instances to fieldMap
        for (String fieldName : fieldNames) {
            fieldTreeMaps.put(fieldName, new TreeMap<>());
        }
        currentField = "ISBN";
    }

    private void initializeGUI() {
        // Opening
        setTitle("Book Browser");
        setSize(500, 470);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        // Panel
        JPanel panel = new JPanel(null);
        getContentPane().add(panel);

        // Select box
        JComboBox<String> s = new JComboBox<>();
        s.addItem("ISBN");
        s.addItem("Author");
        s.addItem("Year");
        s.addItem("Original Title");
        s.addItem("Title");
        s.addItem("Average Rating");
        s.setBounds(X_TEXT, 15, WIDTH_TEXT, HEIGHT_TEXT * 2);
        panel.add(s);

        // TextField
        isbnTextField = new JTextField();
        isbnTextField.setBounds(X_TEXT, 80, WIDTH_TEXT, HEIGHT_TEXT);
        panel.add(isbnTextField);

        authorTextField = new JTextField();
        authorTextField.setBounds(X_TEXT, 130, WIDTH_TEXT, HEIGHT_TEXT);
        panel.add(authorTextField);

        yearTextField = new JTextField();
        yearTextField.setBounds(X_TEXT, 180, WIDTH_TEXT, HEIGHT_TEXT);
        panel.add(yearTextField);

        originalTitleTextField = new JTextField();
        originalTitleTextField.setBounds(X_TEXT, 230, WIDTH_TEXT, HEIGHT_TEXT);
        panel.add(originalTitleTextField);

        titleTextField = new JTextField();
        titleTextField.setBounds(X_TEXT, 280, WIDTH_TEXT, HEIGHT_TEXT);
        panel.add(titleTextField);

        ratingTextField = new JTextField();
        ratingTextField.setBounds(X_TEXT, 330, WIDTH_TEXT, HEIGHT_TEXT);
        panel.add(ratingTextField);

        //Label field
        JLabel orderLabel = new JLabel("Order: ");
        orderLabel.setBounds(X_LABEL, 18, WIDTH_LABEL, HEIGHT_LABEl);
        panel.add(orderLabel);

        isbnLabel = new JLabel("ISBN:");
        isbnLabel.setBounds(X_LABEL, 68, WIDTH_LABEL, HEIGHT_LABEl);
        panel.add(isbnLabel);

        authorLabel = new JLabel("Author:");
        authorLabel.setBounds(X_LABEL, 118, WIDTH_LABEL, HEIGHT_LABEl);
        panel.add(authorLabel);

        yearLabel = new JLabel("Year:");
        yearLabel.setBounds(X_LABEL, 168, WIDTH_LABEL, HEIGHT_LABEl);
        panel.add(yearLabel);

        originalLabel = new JLabel("Orig. Title:");
        originalLabel.setBounds(X_LABEL, 218, WIDTH_LABEL, HEIGHT_LABEl);
        panel.add(originalLabel);

        titleLabel = new JLabel("Title:");
        titleLabel.setBounds(X_LABEL, 268, WIDTH_LABEL, HEIGHT_LABEl);
        panel.add(titleLabel);

        ratingLabel = new JLabel("Avg. Rating");
        ratingLabel.setBounds(X_LABEL, 318, WIDTH_LABEL, HEIGHT_LABEl);
        panel.add(ratingLabel);

        // Button next/previous
        previous = new JButton("< Previous");
        previous.setBounds(200,Y_BUTTON,WIDTH_BUTTON,HEIGHT_BUTTON);
        panel.add(previous);

        next = new JButton("Next >");
        next.setBounds(310,Y_BUTTON,WIDTH_BUTTON,HEIGHT_BUTTON);
        panel.add(next);

        first = new JButton("|<");
        first.setBounds(157,Y_BUTTON,WIDTH_BUTTON / 2, HEIGHT_BUTTON);
        panel.add(first);

        last = new JButton(">|");
        last.setBounds(404,Y_BUTTON,WIDTH_BUTTON / 2, HEIGHT_BUTTON);
        panel.add(last);

        // Action
        s.addActionListener(e -> {
            currentField = (String) s.getSelectedItem();
            currentIndex = 0;
            browseBooks();
        });

        last.addActionListener(e -> showLastBook());
        next.addActionListener(e -> showNextBook());
        previous.addActionListener(e -> showPreviousBook());
        first.addActionListener(e -> showFirstBook());

    }

    private void browseBooks(){
        currentIndex = 0;
        displayBookInfo();
    }

    private void showLastBook(){
        TreeMap<?, Book> selectedBooks = getSelectedTreeMap();
        currentIndex = selectedBooks.size() - 1;
        displayBookInfo();
    }

    private void showFirstBook(){
        currentIndex = 0;
        displayBookInfo();
    }

    private void showNextBook(){
        TreeMap<?, Book> selectedBooks = getSelectedTreeMap();
        if (currentIndex < selectedBooks.size() - 1) {
            currentIndex++;
            displayBookInfo();
        }
    }

    private void showPreviousBook(){
        if(currentIndex > 0){
            currentIndex--;
            displayBookInfo();
        }
    }

    private void displayBookInfo() {
        TreeMap<?, Book> selectedBooks = getSelectedTreeMap();
        Book book = selectedBooks.toValueArray(new Book[selectedBooks.size()])[currentIndex];
        isbnTextField.setText(book.ISBN());
        authorTextField.setText(book.author());
        yearTextField.setText(Integer.toString(book.year()));
        originalTitleTextField.setText(book.originalTitle());
        titleTextField.setText(book.title());
        ratingTextField.setText(Double.toString(book.averageRating()));
        repaint();
        revalidate();
    }

    private TreeMap<?, Book> getSelectedTreeMap(){
        switch (currentField){
            case "Author":
                return authorMap;
            case "ISBN":
                return ISBNMap;
            case "Year":
                return yearMap;
            case "Original Title":
                return originalTitleMap;
            case "Title":
                return titleMap;
            case "Average Rating":
                return ratingMap;
            default:
                return new TreeMap<>();
        }
    }


    //Demo
    public static void main(String[] args) throws FileNotFoundException {
        BookBrowser1 demo = new BookBrowser1("BooksDataFile.txt");
    }

}
