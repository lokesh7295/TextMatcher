public class MatcherResult {
    private final int lineOffset;
    private final int charOffset;

    public MatcherResult(int lineOffset, int charOffset) {
        this.lineOffset = lineOffset;
        this.charOffset = charOffset;
    }

    @Override
    public String toString() {
        return "[lineOffset=" + lineOffset + ", charOffset=" + charOffset + "]";
    }
}
