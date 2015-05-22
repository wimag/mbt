import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ekaterina.Alekseeva on 18-May-15.
 */
public class EventFlowGraph {
    public static ArrayList<EFGNode> nodes = new ArrayList<EFGNode>();
    public static ArrayList<EFGNode> roots = new ArrayList<EFGNode>();
    public static ArrayList<ArrayList<EFGNode>> paths = new ArrayList<ArrayList<EFGNode>>();

    public static EFGNode findNode(String name){
        for (EFGNode i : nodes){
            if (i.name.equals(name)){
                return i;
            }
        }
        return null;
    }

    public static void printNodes(){
        for (EFGNode i : nodes){
            if (i.parent == null) {
                System.out.println(i.name + " " + i.selector + " " + null + " " + i.simple);
            } else {
                System.out.println(i.name + " " + i.selector + " " + i.parent.name + " " + i.simple);
            }
        }
    }

    public static void printPaths(){
        for (ArrayList<EFGNode> i : paths){
            for (EFGNode j : i){
                System.out.print(j.name + " ");
            }
            System.out.println();
        }
    }

    public static void parseGUIGraph (String filename) throws IOException {
        BufferedReader input = new BufferedReader(new FileReader(filename));
        String currentLine;

        while((currentLine = input.readLine()) != null){
//            System.out.println(currentLine);
            EFGNode tmpNode = new EFGNode();
            int len = currentLine.length();
            if (!currentLine.contains("URL")) {
                if (currentLine.contains("label")) {
                    tmpNode.name = currentLine.substring(0, currentLine.indexOf(' '));
                    tmpNode.selector = currentLine.substring(currentLine.indexOf('"')+1, len - 3);
                    nodes.add(tmpNode);
//                    System.out.println(tmpNode.name + " " + tmpNode.selector);
                } else if (currentLine.contains(" -- ")){
                    String[] substrings = currentLine.split(" -- ");
                    substrings[substrings.length-1] = substrings[substrings.length-1].replaceAll(";", "");
//                    for (String i : substrings){
//                        System.out.println(i);
//                    }
                    for (int i = 0; i < substrings.length-1; i++){
                        findNode(substrings[i]).parent = findNode(substrings[i+1]);
                    }
                }
            }
//            System.out.println();
        }
    }

    public static void markSimpleNodes(){
        for (EFGNode i : nodes){
            if (i.parent == null){
                i.simple = true;
                i.root = true;
                roots.add(i);
            } else {
                i.simple = false;
                findNode(i.parent.name).simple = false;
            }
        }
    }

//    public static ArrayList<EFGNode> next_combination (int[] a, int n) {
//        System.out.println("next");
//        int k = a.length;
////        for (int i : a){
////            System.out.print(i + " ");
////        }
////        System.out.println();
//
//        for (int i = k-1 ; i >= 0; i--) {
//            System.out.println(a[i] + " " + (n - k + i + 1));
//            if (a[i] < n - k + i + 1) {
//                a[i]++;
//                for (int j = i + 1; j < k; ++j)
//                    a[j] = a[j - 1] + 1;
//                System.out.println("true");
//                return a;
//            }
//        }
//        System.out.println("false");
//        return null;
//    }

    public static int swap(int a, int b) {  // usage: y = swap(x, x=y);
        return a;
    }

    public static int[] reverse (int[] array, int start, int end){
        int i;
        int m = end - start;
        for (i = 0; i < m/2; i++){
            array[end-i-1] = swap (array[i+start], array[i+start]=array[end-i-1]);
        }
        return array;
    }

    public static void generatePermutations(int length, int firstExcluded, int secondExcluded){
        int n = roots.size()-1;
        int[] combinations = new int [length];
        for (int i = 0; i < length; i++){
                combinations[i] = i;
        }

        System.out.println("length " + length);

        while(true){
//            for(int i=0;i<length;i++) //�������� ��������� ������������������
//                System.out.print(combinations[i] + " ");
//            System.out.println();

            boolean hasExcluded = false;
            for(int i = 0; i < length; i++){
                if (combinations[i] == firstExcluded || combinations[i] == secondExcluded){
                    hasExcluded = true;
                    break;
                }
            }

            if(!hasExcluded){
//                System.out.println("not has excluded");
                int min;
                boolean ifCheck = false;
                int [] permutations = combinations.clone();
                while (true){
                    for (int i = length-2; i >= 0; i--){ //������������� ������ � �����
                        ifCheck = false;
                        if (permutations[i] < permutations[i+1]) { // ���� ��������� "����������" (��-�� ����������� �� �� ����������� ������� � ������-�� ��-�� X)
                            min = i+1;
                            for (int j = i+1; j < length; j++){
                                if (permutations[j] <= permutations[min] && permutations[j] > permutations[i]) {//���� ����� ���������� ��-��� ����������, ������� X
                                    min = j;
                                }
                            }
                            permutations[min] = swap (permutations[i], permutations[i]=permutations[min]); //������ �� �������
                            if ((i+1)!=(length-1)) {
                                permutations = reverse (permutations, i+1, length);//������������� ��� ���������� ��-��
                            }
                            ifCheck = true;
                            break;
                        }
                    }
                    if (!ifCheck) {
                        break;
                    }
                    ArrayList<EFGNode> curPath = new ArrayList<EFGNode>();
                    curPath.add(roots.get(firstExcluded));
                    for(int i = 0; i < length; i++) {
                        curPath.add(roots.get(permutations[i]));
                        System.out.print(permutations[i] + " ");
                    }
                    curPath.add(roots.get(secondExcluded));
                    paths.add(curPath);
                    System.out.println();
                }

                ArrayList<EFGNode> curPath = new ArrayList<EFGNode>();
                curPath.add(roots.get(firstExcluded));
                for(int i = 0; i < length; i++) {
                    curPath.add(roots.get(combinations[i]));
                    System.out.print(combinations[i] + " ");
                }
                curPath.add(roots.get(secondExcluded));
                paths.add(curPath);
                System.out.println();
            }

            int i;
            for(i=length-1;i>=0 && combinations[i]==n+i-length+1;i--); //���� ������ ������ �������, �� ��������� ������������� ��������

            if(i==-1) break; //���� �� �����, �� ����������� ������.

            combinations[i]++; //���� �����, �� ����������� ��� �� 1

            for(int j=i+1;j<length;j++) //� ��������� ������ �����

                combinations[j]=combinations[j-1]+1; //���������� ���������� ����������.

        }
    }

    public static void findPathBetweenTwoNodes(int a, int b){
        int r = 0;
        int n = roots.size();
        ArrayList<EFGNode> curPath = new ArrayList<EFGNode>();
        curPath.add(roots.get(a));
        curPath.add(roots.get(b));
        paths.add(curPath);

//        r++;
        while (r+2 < n){
            r++;
            generatePermutations(r, a, b);

        }
    }

    public static void findPathsBetweenRoots(){
//        for (int i = 0; i < roots.size(); i++){
//            for (int j = 0; j < roots.size(); j++){
//                if (i != j){
//                    findPathBetweenTwoNodes(i, j);
//                }
//            }
//        }
        findPathBetweenTwoNodes(0, 4);
    }

    public static void findPathsInTree(EFGNode root){

    }

    public static void main(String[] args) throws IOException {
        int graphCounter = 87;
//        String filename = "GUIgraph"+graphCounter;
        String filename = "GUIgraph";

        parseGUIGraph(filename);

        markSimpleNodes();

        findPathsBetweenRoots();

//        printNodes();

        printPaths();
    }
}