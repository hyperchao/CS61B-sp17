public class OffByOne implements CharacterComparator {
    @Override
    public boolean equalChars(char a, char b) {
        return (a - b) == 1 || (a - b) == -1;
    }
}
