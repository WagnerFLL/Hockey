class Line{
    int x1, x2, y1, y2;

    Line(int x1, int y1, int x2, int y2){
        if (x1 < x2){
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
        } else {
            this.x1 = x2;
            this.x2 = x1;
            this.y1 = y2;
            this.y2 = y1;
        }
    }
}