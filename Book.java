public record Book(String ISBN, String author, int year, String originalTitle, String title, double averageRating) implements Comparable<Book> {
    @Override
    public int compareTo(Book o) {
        return this.ISBN.compareTo(o.ISBN);
    }
}