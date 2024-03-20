package aed.tables;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Treap <Key extends Comparable<Key>, Value> {
    No root;
    int size;
    Random prioridade;
    int modification;

    class No{
        Key k;
        Value v;
        int prioridade;
        int size;
        No esquerda;
        No direita;
        No pai;

        public No(Key k , Value v,int size,int r, No e, No d, No pai){
            this.k = k;
            this.v = v;
            this.size = size;
            this.prioridade = r;
            this.esquerda = e;
            this.direita = d;
            this.pai = pai;
        }

        public String toString() {
            return "[k:" + this.k + " v:" + this.v + " p:" + this.prioridade + " s:" + this.size + "]";
        }
    }

    public Treap() {
        this.size = 0;
        this.root = null;
        this.prioridade = new Random();
        this.modification = 0;
    }

    public Treap(Random r) {
        this.size = 0;
        this.root = null;
        this.prioridade = r;
        this.modification  =0;
    }

    //_----------------------------- Função size ------------------------------------- //
    public int size(){return this.size;}
    //_------------------------------------------------------------------ //

    //_----------------------------- Função Contains_Key ------------------------------------- //
    public boolean containsKey(Key k){
        Value a = get(k);
        boolean result = false;
        if(a != null){result =true;}
        return result;
    }
    //_--------------------------------------------------------------------------------------- //
    //_----------------------------- Função get ------------------------------------- //
    public Value get(Key k) {
        return get(this.root, k);
    }

    private Value get(No n, Key k) {
        if(n == null) return null;
        int cmp = k.compareTo(n.k);
        if (cmp < 0) return get(n.esquerda,k);
        else if(cmp > 0) return get(n.direita,k);
        else {
            return n.v;
        }
    }

    //_------------------------------------------------------------------------------ //
    //_----------------------------- Função put ------------------------------------- //

    public No rotate_left(No pai){
        No A = pai;
        int x = 0,y = 0, z = 0;
        No B = A.direita.esquerda;
        pai = A.direita;
        pai.pai = A.pai;
        pai.esquerda = A;
        pai.esquerda.pai = pai;
        if(B != null){
            pai.esquerda.direita = B;
            pai.esquerda.direita.pai = pai.esquerda;
        }
        else {
            pai.esquerda.direita = null;
        }
        if(pai.pai != null){
            if(pai.pai.direita == A){
                pai.pai.direita = pai;
            }
            else if(pai.pai.esquerda == A){
                pai.pai.esquerda = pai;
            }
        }
        else {
            this.root = pai;
        }
        //-------------- Atualizar size
        if(pai.esquerda.esquerda != null){
            x = pai.esquerda.esquerda.size;
        }
        if(pai.esquerda.direita != null){
            y = pai.esquerda.direita.size;
        }
        pai.esquerda.size = x + y + 1;
        if(pai.direita != null){
            z = pai.direita.size;
        }
        pai.size = pai.esquerda.size + z + 1;
        return pai;
    }

    public No rotate_right(No pai){
        No A = pai;
        int x = 0, y = 0,z  = 0;
        No B = A.esquerda.direita;
        pai = A.esquerda;
        pai.pai = A.pai;
        pai.direita = A;
        pai.direita.pai = pai;
        if(B != null){
            pai.direita.esquerda = B;
            pai.direita.esquerda.pai = pai.direita;
            x = pai.direita.esquerda.size;
        }
        else{
            pai.direita.esquerda = null;
        }
        if(pai.pai == null){
            this.root = pai;
        }
        else {
            if(pai.pai.direita == A){
                pai.pai.direita = pai;
            }
            else if(pai.pai.esquerda == A){
                pai.pai.esquerda = pai;
            }
        }
        //-------------- Atualizar size
        if(pai.direita.direita != null){
            y = pai.direita.direita.size;
        }
        pai.direita.size = x + y + 1;
        if(pai.esquerda != null){
            z = pai.esquerda.size;
        }
        pai.size = pai.direita.size + z + 1;
        return pai;
    }


    public void ordenar (No pai, No filho,int x){
        if(filho.prioridade <= pai.prioridade){}// Se a prioridade do filho ser menor do que a do pai, não se faz nada
        else if(x == 1){ // Se o filho tiver na direita, faz rotate left
            pai = rotate_left(pai);
            if(pai.pai != null && pai.pai.direita == pai){// Se o pai é um filho direita
                ordenar(pai.pai,pai,1);
            }
            else if(pai.pai != null && pai.pai.esquerda == pai){// Se o pai é um filho esquerda
                ordenar(pai.pai,pai,-1);
            }
        }
        else if(x == -1){ // Se o filho tiver na esquerda, faz rotate right
            pai = rotate_right(pai);
            if(pai.pai != null && pai.pai.direita == pai){// Se o pai é um filho direita
                ordenar(pai.pai,pai,1);
            }
            else if(pai.pai != null && pai.pai.esquerda == pai){// Se o pai é um filho esquerda
                ordenar(pai.pai,pai,-1);
            }
        }

    }

    public void criar_novo_NO(No n, Key k,Value v, No anterior, int x){ //Função que cria um No
        No A = new No(k,v,1,prioridade.nextInt(),null,null,anterior);
        if(x == -1){// Novo Filho está na esquerda do pai
            anterior.esquerda = A;
            aumentar_size(anterior);
            ordenar(anterior,anterior.esquerda,-1);
        }
        else if(x == 1){// Novo Filho está na direita do pai
            anterior.direita = A;
            aumentar_size(anterior);
            ordenar(anterior,anterior.direita,1);
        }
        this.size++;
        this.root.size = this.size;
    }

    public void aumentar_size(No anterior){//aumenta o size que cada No
        if(anterior.pai != null){
            anterior.size++;
            aumentar_size(anterior.pai);
        }
    }

    public void put(No n, Key k, Value v,No anterior,int x) {
        if(n == null){ //Em caso de a chave não existir
            criar_novo_NO(n,k,v,anterior,x);
        }
        else{
            int cmp = k.compareTo(n.k);
            if(cmp < 0) put(n.esquerda, k, v,n,-1);
            else if(cmp > 0) put(n.direita, k, v,n,1);
            else { // Em caso de existir a chave k
                if(v == null){ // Em caso de v ser null, eliminamos a chave k
                    delete(k);
                }
                else { // Substituimos pelo novo v
                    n.v = v;
                }
            }
        }

    }

    public void put(Key k, Value v){
        if(this.root == null){
            this.root = new No(k,v,size,prioridade.nextInt(),null,null,null);
            this.root.size++;
            this.size++;
        }
        else{
            put(this.root,k,v,null,0);
        }
    }

    //_------------------------------------------------------------------------------------- //


    //_---------------------------------Delete -------------------------------------------------------- //

    public void remover(No A, No B){
        if(A.esquerda == null && A.direita == null){
            if(A.pai.direita == A){
                A.pai.direita = null;
                diminuir(A.pai);
                this.size--;
                this.root.size = this.size;

            }
            else if(A.pai.esquerda == A){
                A.pai.esquerda = null;
                diminuir(A.pai);
                this.size--;
                this.root.size = this.size;

            }
        }
        else if(A.esquerda != null && A.direita != null){
            if(A.esquerda.prioridade > A.direita.prioridade){
                A = rotate_right(A);
                A = A.direita;
                remover(A,null);
            }
            else{
                A = rotate_left(A);
                A = A.esquerda;
                remover(A,null);
            }
        }
        else if(A.esquerda != null ){
            A = rotate_right(A);
            A = A.direita;
            remover(A,null);
        }
        else{
            A = rotate_left(A);
            A = A.esquerda;
            remover(A,null);
        }
    }


    public void remover(No A){
        remover(A,A.esquerda);
    }

    public No delete(No n,Key k,Value v, No anterior, int x){ // Descobre se a chave existi
        if(n != null){// Se o n é diferente null
            int cmp = k.compareTo(n.k);
            if(cmp < 0) return delete(n.esquerda, k, v,n,-1);
            else if(cmp > 0) return delete(n.direita, k, v,n,1);
            else {
                n.prioridade = (int) Double.NEGATIVE_INFINITY;
                return n;
            }
        }
        return n;
    }

    //Remove a chave recebida, se não existir não faz nada
    public void delete (Key k){
        No A = delete(this.root,k,null,null,0);
        if(A != null){
            remover(A);
        }
    }

    //_---------------------------------Split -------------------------------------------------------- //

    public void criar_novo_NO_select(No n, Key k,Value v, No anterior, int x){
        No A = new No(k,v,1,(int) Double.POSITIVE_INFINITY,null,null,anterior);
        if(x == -1){// Novo Filho está na esquerda
            anterior.esquerda = A;
            aumentar_size(anterior);
            ordenar(anterior,anterior.esquerda,-1);
        }
        else if(x == 1){// Novo Filho está na direita
            anterior.direita = A;
            aumentar_size(anterior);
            ordenar(anterior,anterior.direita,1);
        }
        this.size++;
        this.root.size = this.size;
    }

    public No split_1(No n, Key k, Value v,No anterior,int x){
        if(n == null){
            criar_novo_NO_select(n,k,v,anterior,x);
            this.size++;
            this.root.size = this.size;
        }
        else{
            int cmp = k.compareTo(n.k);
            if(cmp < 0) split_1(n.esquerda, k, v,n,-1);
            else if(cmp > 0) split_1(n.direita, k, v,n,1);
            else {
                n.prioridade = (int) Double.POSITIVE_INFINITY;
                if( n.pai != null &&  n.pai.direita == n){
                    ordenar(anterior,n,1);
                }
                else if(n.pai != null && n.pai.esquerda == n){
                    ordenar(anterior,n,-1);
                }
            }
        }
        return root;
    }

    public No copy_1(No root) {//Copia todos os elementos a partir de um No recebido
        if (root == null) {
            return null;
        }
        No root_copy = new No(root.k,root.v, root.size, root.prioridade,root.esquerda,root.direita,root.pai);
        root_copy.esquerda = copy_1(root.esquerda);
        root_copy.direita = copy_1(root.direita);
        return root_copy;
    }

    //Mete a chave a recebida como a raiz , e retorna um Array Treap com a parte esquerda e direita
    @SuppressWarnings("rawtypes")
    public Treap[] split(Key k) {
        Treap[] K = new Treap[2];
        Treap<Key,Value> Esquerda = new Treap<>();
        Treap<Key,Value> Direita = new Treap<>();
        if(this.root != null){
            No A = split_1(this.root, k,null,null,0);
            if(A.esquerda != null){
                Esquerda.root = copy_1(A.esquerda);
                Esquerda.root.size = A.esquerda.size;
                Esquerda.size = A.esquerda.size;
                Esquerda.root.pai = null;
            }
            if(A.direita != null){
                Direita.root = copy_1(A.direita);
                Direita.root.size = A.direita.size;
                Direita.size = A.direita.size;
                Direita.root.pai = null;
            }
            K[0] = Esquerda;
            K[1] = Direita;
        }
        return K;
    }

    ////- ---------------------  keys Min e Max ------///

    public Key Min(No A){
        Key K = null;
        if(A.esquerda != null){
            return Min(A.esquerda);
        }
        else{
            K = A.k;
        }
        return K;
    }

    public Key min(){
        Key k =null;
        if(this.root != null){
            k =  Min(this.root);
        }
        return k;
    }

    public Key Max(No A){
        Key K = null;
        if(A.direita != null){
            return Max(A.direita);
        }
        else{
            K = A.k;
        }
        return K;
    }

    public Key max(){
        Key k =null;
        if(this.root != null){
            k =  Max(this.root);
        }
        return k;
    }

    ////- ----------------------------------------------------------------------------------------------------

    ////- ---------------------  Delete keys Min e Max ------///

    public void diminuir(No A){
        if(A.pai != null){
            A.size--;
            diminuir(A.pai);
        }
    }

    public void deleteMin(No A){
        if(this.root == A && A.esquerda == null && A.direita !=null){
            this.root = A.direita;
            this.root.pai = null;
            this.size--;
            this.root.size = this.size;
        }
        else if(this.root == A && A.esquerda == null){
            this.root = null;
            this.size--;
        }
        else if(A.esquerda != null){
            deleteMin(A.esquerda);
        }
        else if(A.direita != null){
            A.pai.esquerda = A.direita;
            A.direita.pai = A.pai;
            diminuir(A);
            this.size--;
            this.root.size = this.size;

        }
        else{
            A.pai.esquerda = null;
            diminuir(A);
            this.size--;
            this.root.size = this.size;

        }

    }
    public void deleteMin(){
        if(this.root != null){
            deleteMin(this.root);
        }

    }
    public void deleteMax(No A ){
        if(this.root == A && A.direita == null && A.esquerda != null){
            this.root = A.esquerda;
            this.root.pai = null;
            this.size--;
            this.root.size = this.size;

        }
        else if(this.root == A && A.direita == null ){
            this.root = null;
            this.size--;
        }
        else if(A.direita == null && A.esquerda != null){
            A.pai.direita = A.esquerda;
            A.esquerda.pai = A.pai;
            diminuir(A);
            this.size--;
            this.root.size = this.size;

        }
        else if(A.direita != null) {
            deleteMax(A.direita);
        }
        else{
            A.pai.direita = null;
            diminuir(A);
            this.size--;
            this.root.size = this.size;
        }
    }
    public void deleteMax(){
        if(this.root != null){
            deleteMax(this.root);
        }
    }
    ////-----------------------------------------------------------------------------------------------------

    ////---------------------- Rank ----------------------------------------------------------- ------///


    public int rank(No A, Key k,int result){
        if(A.k.compareTo(k) == 0){ // Qnd a chave for igual,soma ao result o size da esquerda
            if(A.esquerda != null){
                result += A.esquerda.size;
            }
        }
        else if(A.k.compareTo(k) < 0){  //Se for maior vai para a direita e se a esquerda soma ao result
            if(A.esquerda != null){ //Se a esquerda existir somamos ao result , o size da esquerda
                result += A.esquerda.size;
            }
            result += 1; //Tem de somar pelo menos o No que é menor
            if(A.direita != null ){ // Se a direita existir, vai para a direita
                return rank(A.direita,k,result);
            }
            else{
                return result;
            }
        }
        else{ //Se for menor vai para esquerda, se o filho da esquerda existir
            if(A.esquerda != null){
                return rank(A.esquerda,k,result);
            }
        }
        return result;
    }

    public int rank(Key k){
        int result = 0;
        if(this.root != null){
            result = rank(this.root, k,result);
        }
        return result;
    }

    ////-----------------------------------------------------------------------------------------------------
    ////---------------------- Size(min, max) ----------------------------------------------------------- ------///

    public int menor_ou_igual(No A, Key k,int result){
        if(A.k.compareTo(k) == 0){
            if(A.esquerda != null){
                result += A.esquerda.size;
            }
            result += 1;
        }
        else if(A.k.compareTo(k) < 0){
            if(A.esquerda != null){
                result += A.esquerda.size;
            }
            result += 1;
            if(A.direita != null ){
                return menor_ou_igual(A.direita,k,result);
            }
            else{
                return result;
            }
        }
        else{
            if(A.esquerda != null){
                return menor_ou_igual(A.esquerda,k,result);
            }
        }
        return result;
    }
    public int size(Key min,Key max) {
        if (this.root != null) {
            int result = menor_ou_igual(this.root, max, 0);
            int a = rank(min);
            result = result - a ;
            return result;
        }
        return 0;
    }

    ////- ----------------------------------------------------------------------------------------------------

    //---------------------- Select(int n) -----------------------------------------------------------------//

    protected boolean menor(Key v, Key w)
    {
        return v.compareTo(w) < 0;
    }

    protected void trocar(List<Key> a, int i, int j)
    {
        Key t = a.get(i);
        Key m = a.get(j);
        a.set(i,m);
        a.set(j,t);
    }

    public boolean isSorted_1(List<Key> a)
    {
        for (int i = 1; i < this.size; i++)
        {
            if (menor(a.get(i),a.get(i-1))) return false;
        }
        return true;
    }

    public void ordenar(List<Key> a) {
        int n = this.size; // numero de elementos no array
        int d = n-1;
        int v = 1;
        int k  = 0;
        while(d > 0 && k < 2){
            for (int i = 0; i < v;i++ ){
                if(menor(a.get(d+i),a.get(i))){trocar(a,i+d,i);}
            }
            if(d > 1 ){d = (int) (d * 0.77);}
            v = n-d;
            if(d == 1 && isSorted_1(a)){k++;}
        }
    }

    public List<Key> copiar_as_keys(List<Key> K,No A) {
        if (A == null) {
            return null;
        }
        K.add(A.k);
        copiar_as_keys(K,A.esquerda);
        copiar_as_keys(K,A.direita);
        return K;
    }


    public Key select(int n){
        Key result = null;
        if(n < this.size && n >= 0 && this.root != null){
            List<Key> B = new ArrayList<>();
            B = copiar_as_keys(B,this.root);
            ordenar(B);
            result = B.get(n);
        }
        return result;
    }

    ////- ----------------------------------------------------------------------------------------------------

    //_____________________________Copia os Nos da arvore_________________________________//

    public List<No> copiar_NOs(List<No> K,No A) {
        if (A == null) {
            return null;
        }
        K.add(A);
        copiar_NOs(K,A.esquerda);
        copiar_NOs(K,A.direita);
        return K;
    }
    //_____________________________Função inventada por mim para ordenar_________________________________//

    protected void exchange(List<No> a, int i, int j) {
        No t = a.get(i);
        No m = a.get(j);
        a.set(i,m);
        a.set(j,t);
    }

    public boolean isSorted(List<No> a) {
        for (int i = 1; i < this.size; i++) {
            if (less(a.get(i).k,a.get(i-1).k)) return false;
        }
        return true;
    }
    private boolean less(Key v, Key w) {return v.compareTo(w) < 0;}
    public void sort(List<No> a) {
        int n = this.size; // numero de elementos no array
        int d = n-1;
        int v = 1;
        int k  = 0;
        while(d > 0 && k < 2){
            for (int i = 0; i < v;i++ ){
                if(less(a.get(i+d).k,a.get(i).k)){
                    exchange(a,i+d,i);
                }
            }
            if(d > 1 ){d = (int) (d * 0.77);}
            v = n-d;
            if(d == 1 && isSorted(a)){k++;}
        }
    }
    //_____________________________Iterable Keys_________________________________//


    public List<Key> copiar_keys(List<Key> K,List<No> A) {
        for (int i = 0; i < A.size(); i++){
            K.add(A.get(i).k);
        }
        return K;
    }


    public Iterable<Key> keys() {
        List<No> K = new ArrayList<>();
        K = copiar_NOs(K,this.root);
        sort(K);
        List<Key> M = new ArrayList<>();
        M = copiar_keys(M,K);
        this.size = M.size();
        return M;
    }
    //_____________________________Iterable Values_________________________________//


    public List<Value> copiar_values(List<Value> K,List<No> A) {
        for (int i = 0;i < size; i++){
            K.add(A.get(i).v);
        }
        return K;
    }

    public Iterable<Value> values() {
        List<No> K = new ArrayList<>();
        K = copiar_NOs(K,this.root);
        sort(K);
        List<Value> M = new ArrayList<>();
        if(this.root != null){
            M = copiar_values(M,K);
        }
        return M;
    }

    //_____________________________Iterable Prioridade_________________________________//

    public List<Integer> copiar_prioridade(List<Integer> K,List<No> A) {
        for (int i = 0;i < size; i++){
            K.add(A.get(i).prioridade);
        }
        return K;
    }

    public Iterable<Integer> priorities() {
        List<No> K = new ArrayList<>();
        K = copiar_NOs(K,this.root);
        sort(K);
        List<Integer> M = new ArrayList<>();
        if(this.root != null){
            M = copiar_prioridade(M,K);
        }
        return M;
    }

    //_____________________________Iterable keys min e max_________________________________//


    public List<Key> copiar_keys_entre_min_e_max(List<Key> K,List<No> A,Key min, Key max) {
        for (int i = 0;i < size; i++){
            if(min.compareTo(A.get(i).k) <= 0 && max.compareTo(A.get(i).k) >= 0 ) {
                K.add(A.get(i).k);
            }
        }
        return K;
    }

    public Iterable<Key> keys(Key min, Key max) {
        List<No> K = new ArrayList<>();
        K = copiar_NOs(K,this.root);
        sort(K);
        List<Key> M = new ArrayList<>();
        if(this.root != null){
            M = copiar_keys_entre_min_e_max(M,K,min,max);
        }
        return M;
    }


    //_____________________________ShallowCopy_________________________________//
    public No copy(No root) {
        if (root == null) {
            return null;
        }
        No root_copy = new No(root.k,root.v, root.size, root.prioridade,root.esquerda,root.direita,root.pai);
        root_copy.esquerda = copy(root.esquerda);
        root_copy.direita = copy(root.direita);
        return root_copy;
    }
    public Treap<Key,Value> shallowCopy(){
        Treap<Key,Value> A = new Treap<Key,Value>();
        if(modification == 0){
            A.root = copy(this.root);
            A.size = this.size;
            modification++;
        }
        return A;
    }

    //____________________________________________________________________________________//

    //--------------------Print de uma arvore -------------------------------------------//
    public void print(No root)
    {
        List<List<String>> lines = new ArrayList<List<String>>();

        List<No> level = new ArrayList<No>();
        List<No> next = new ArrayList<No>();

        level.add(root);
        int nn = 1;

        int widest = 0;

        while (nn != 0) {
            List<String> line = new ArrayList<String>();

            nn = 0;

            for (No n : level) {
                if (n == null) {
                    line.add(null);

                    next.add(null);
                    next.add(null);
                }
                else {
                    String aa = (String) n.v;
                    line.add(aa);
                    if (aa.length() > widest) widest = aa.length();

                    next.add(n.esquerda);
                    next.add(n.direita);

                    if (n.esquerda != null) nn++;
                    if (n.direita != null) nn++;
                }
            }

            if (widest % 2 == 1) widest++;

            lines.add(line);

            List<No> tmp = level;
            level = next;
            next = tmp;
            next.clear();
        }

        int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
        for (int i = 0; i < lines.size(); i++) {
            List<String> line = lines.get(i);
            int hpw = (int) Math.floor(perpiece / 2f) - 1;

            if (i > 0) {
                for (int j = 0; j < line.size(); j++) {

                    // split node
                    char c = ' ';
                    if (j % 2 == 1) {
                        if (line.get(j - 1) != null) {
                            c = (line.get(j) != null) ? '┴' : '┘';
                        } else {
                            if (j < line.size() && line.get(j) != null) c = '└';
                        }
                    }
                    System.out.print(c);

                    // lines and spaces
                    if (line.get(j) == null) {
                        for (int k = 0; k < perpiece - 1; k++) {
                            System.out.print(" ");
                        }
                    } else {

                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? " " : "─");
                        }
                        System.out.print(j % 2 == 0 ? "┌" : "┐");
                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? "─" : " ");
                        }
                    }
                }
                System.out.println();
            }

            // print line of numbers
            for (int j = 0; j < line.size(); j++) {

                String f = line.get(j);
                if (f == null) f = "";
                int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
                int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

                // a number
                for (int k = 0; k < gap1; k++) {
                    System.out.print(" ");
                }
                System.out.print(f);
                for (int k = 0; k < gap2; k++) {
                    System.out.print(" ");
                }
            }
            System.out.println();

            perpiece /= 2;
        }
    }

    ///.-------------------------- Prof functions -----------------------------------------//
    //helper method that uses the treap to build an array with a heap structure
    private void fillHeapArray(No n, Object[] a, int position) {
        if(n == null) return;

        if(position < a.length)
        {
            a[position] = n;
            fillHeapArray(n.esquerda,a,2*position);
            fillHeapArray(n.direita,a,2*position+1);
        }
    }

    //if you want to use a different organization that a set of nodes with pointers, you can do it, but you will have to change
    //this method to be consistent with your implementation
    private Object[] getHeapArray() {
        //we only store the first 31 elements (position 0 is not used, so we need a size of 32), to print the first 5 rows of the treap
        Object[] a = new Object[32];
        fillHeapArray(this.root,a,1);
        return a;
    }

    private void printCentered(String line) {
        //assuming 120 characters width for a line
        int padding = (120 - line.length())/2;
        if(padding < 0) padding = 0;
        String paddingString = "";
        for(int i = 0; i < padding; i++)
        {
            paddingString +=" ";
        }

        System.out.println(paddingString + line);
    }

    //this is used by some of the automatic tests in Mooshak. It is used to print the first 5 levels of a Treap.
    //feel free to use it for your own tests
    public void printTreapBeginning() {
        Object[] heap = getHeapArray();
        int size =size();
        int printedNodes = 0;
        String nodeToPrint;
        int i = 1;
        int nodes;
        String line;

        //only prints the first five levels
        for (int depth = 0; depth < 5; depth++) {
            //number of nodes to print at a particular depth
            nodes = (int) Math.pow(2, depth);
            line = "";
            for (int j = 0; j < nodes && i < heap.length; j++) {
                if (heap[i] != null) {
                    nodeToPrint = heap[i].toString();
                    printedNodes++;
                } else {
                    nodeToPrint = "[null]";
                }
                line += " " + nodeToPrint;
                i++;
            }

            printCentered(line);
            if (i >= heap.length || printedNodes >= size) break;
        }

    }


    ///.------------------------------------------------------------------------------------------------//
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Random r = new Random();
        Treap<Integer,String > K = new Treap<>(r);
        K.put(12,"12");
        K.put(26,"26");
        K.put(-581,"-581");
        K.put(4,"4");
        K.put(44,"44");
        K.put(14,"14");
        K.put(13,"13");
        K.put(15,"15");
        K.put(33,"33");
        K.put(323,"323");
        K.put(-10,"-10");
        K.put(-11320,"-11320");
        K.put(1320,"1320");
        K.put(-1120,"-1120");
        K.put(-1320,"-1320");
        K.put(1320,"1320");
        K.put(1120,"1120");
        K.put(-120,"-120");
        K.put(-1132320,"-1132320");



        //-------------------------- Print da arvore  --------------------------------------------- //
        K.print(K.root);
        //--------------------------------------------------------------------------------- //





        //-------------------------- Testes para Eficiencia do put  ----------------------- //

         for (int i = 0 ; i < 50000; i++){
         K.put(i,"asdadasdas");
        }
        for (int i = 0 ; i < 50000; i++){
            K.delete(i);
        }

        // for (int i = 0 ; i < 25000; i++){
        //    K.put(i,"asdadasdas");
        //}



        // for (int i = 0 ; i < 50000; i++){
        //    K.put(i,"asdadasdas");
        //}

        //---------------------------------------------------------------------- //




        //-------------------------- Testes para Delete  ----------------------- //

        //K.delete(213);
        //K.delete(1320);
        //K.delete(-1320);
        //K.delete(12);
        //K.delete(13);
        //K.delete(5);
        //K.delete(4);
        //K.delete(33);
        //K.delete(26);
        //K.delete(-581);

        //---------------------------------------------------------------------- //


        //-------------------------- Testes para Iterable  ----------------------- //
        //System.out.println("Iterable: ");
  /*     List M = (List) K.keys();
        M.forEach((Key) -> {
            System.out.println("Key: " + Key);
        });


/*
     List N = (List) K.values();
        N.forEach((Value) -> {
            System.out.println("Value: " + Value);
        });
        List O = (List) K.priorities();
        O.forEach((Prioridade) -> {
            System.out.println("Prioridade: " + Prioridade);
        });
*/

        /*List P = (List) K.keys(-10,43);
        P.forEach((Keys) -> {
            System.out.println("Keys(-10,43): " + Keys);
        });*/





        //-------------------------------------------------------------------- //


        //-------------------------- Testes para Slipt ----------------------- //


        // K.printTreapBeginning();
        //        // K.print(K.root);
        //
        //        //Treap[] A = K.split(-1132320);
        //       // K.print(K.root);
        //       // System.out.println("Esquerda");
        //        //K.print(A[0].root);
        //
        //        //System.out.println("Direita");
        //        //K.print(A[1].root);
        //        //K.printTreapBeginning();


        //-------------------------------------------------------------------- //


        //-------------------------- Testes para Rank  ----------------------- //

        // System.out.println("Rank: " + K.rank(1));
        // System.out.println("Rank: " + K.rank(232));
        // System.out.println("Rank: " + K.rank(23));
        // System.out.println("Rank: " + K.rank(32));
        // System.out.println("Rank: " + K.rank(5329));
        // System.out.println("Rank: " + K.rank(-325));
        // System.out.println("Rank: " + K.rank(-2333432));
        // System.out.println("Rank: " + K.rank(-423));
        // System.out.println("Rank: " + K.rank(0));
        // System.out.println("Rank: " + K.rank(420));


        //-------------------------- Testes para KeyMin e KeyMax ----------------------- //
/*
       System.out.println("KeyMin: " + K.min());
        K.deleteMin();
        System.out.println("KeyMin: " + K.min());
        K.print(K.root);
   //     System.out.println("Size_63:" + K.root.esquerda.esquerda.size);
        K.deleteMin();
        System.out.println("KeyMin: " + K.min());
        K.print(K.root);
        K.printTreapBeginning();


        K.deleteMin();
        System.out.println("KeyMin: " + K.min());
        K.print(K.root);

        K.deleteMin();
        System.out.println("KeyMin: " + K.min());
        K.print(K.root);
        K.deleteMin();
        System.out.println("KeyMin: " + K.min());
        K.print(K.root);
        K.deleteMin();
        System.out.println("KeyMin: " + K.min());
        K.print(K.root);
        K.deleteMin();
        System.out.println("KeyMin: " + K.min());
        K.print(K.root);
*/
     /*

        K.deleteMin();
        System.out.println("KeyMin: " + K.min());

        K.print(K.root);
        K.deleteMin();
        System.out.println("KeyMin: " + K.min());
        K.print(K.root);
        K.deleteMin();
        System.out.println("KeyMin: " + K.min());
        K.print(K.root);

        K.deleteMin();
        System.out.println("KeyMin: " + K.min());
        K.print(K.root);
        K.deleteMin();

        System.out.println("Size_63:" + K.root.esquerda.size);

        /*System.out.println("KeyMin: " + K.min());
        K.print(K.root);*/


/*
        System.out.println("KeyMax: " + K.max());
        K.deleteMax();
        System.out.println("KeyMax: " + K.max());
        K.print(K.root);
        K.deleteMax();
        System.out.println("KeyMax: " + K.max());
        K.print(K.root);
        K.deleteMax();
        System.out.println("KeyMax: " + K.max());
        K.print(K.root);
        K.deleteMax();
        System.out.println("KeyMax: " + K.max());
        K.print(K.root);
        K.deleteMax();
        System.out.println("KeyMax: " + K.max());
        K.print(K.root);
        K.deleteMax();
        System.out.println("KeyMax: " + K.max());
        K.print(K.root);
        K.deleteMax();
        System.out.println("KeyMax: " + K.max());
        K.print(K.root);

        K.deleteMax();
        System.out.println("KeyMax: " + K.max());
        K.print(K.root);
*/


//----------------------------------------------------------------------------------------------------------------------


        //----------------------------------------Size -------------------------- //
        System.out.println("Size: " + K.size());

        //------------------------------- ContainsKeys -------------------------- //
        // System.out.println("ContainsKeys: " + K.containsKey(3232));
        // System.out.println("ContainsKeys: " + K.containsKey(232));

        //--------------Size(min,max) ----------------------------------------------- //
        // System.out.println("Size(min,max): " + K.size(-134,32));
        // System.out.println("Size: " + K.size(4,14));
        // System.out.println("Size: " + K.size(4,14));

        //---------------------------------- Select   ---------------------- ------ //

        // System.out.println("Select: " + K.select(3));


        //-------------------------- ShallowCopy   ------------------ ------ //

/*
        Treap M = K.shallowCopy();
        M.print(M.root);
        K.put(23,"efds");
        K.print(K.root);
        System.out.println("..........");
        M = K.shallowCopy();
        M.print(M.root);*/



        long end = System.currentTimeMillis();
        NumberFormat formatter = new DecimalFormat("#0.000");
        System.out.print("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");
    }
}