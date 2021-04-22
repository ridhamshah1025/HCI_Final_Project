package com.example.hci_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.gesture.GestureLibrary;
import android.gesture.GestureLibraries;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.gesture.Prediction;
import android.gesture.Gesture;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnGesturePerformedListener {
    List<String> Characters;
    List<String> Names;
    Map<String, List<String>> Contacts;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    Button button12;
    LayoutInflater layoutInflater;
    int lastExpandedPosition = -1;
    private GestureLibrary GesLib;
    String predict;
    long currentTime;
    long startButtonTime;
    long childClickTime;
    ArrayList<String> taskList = new ArrayList<String>();
    ArrayList<Long> taskTime = new ArrayList<Long>();
    ArrayList<Integer> taskNumbers = new ArrayList<Integer>();
    ArrayList<String> taskDoneList = new ArrayList<String>();
    ArrayList<String> drawCharacterList = new ArrayList<String>();
    ArrayList<Integer> drawCharacterPosition = new ArrayList<Integer>();
    String lastCharacterList =null;  // first character
    String previousCharacter = null; //second character
    int lastCharacterPosition;
    String currentPredict =null;
    int taskNumber;
    int counter;
    int currentIndex;
    int showIndex;

    int secondCharacter=0;


    Object selectedChildItem;
    long getSelectedId;
    long getSelectedPosition;
    Object getSelectedItem;
    long getSelectedItemId;
    int getSelectedItemPosition;
    StringBuilder data = new StringBuilder();


//    RelativeLayout layout;
//    public boolean check_con=true;



    @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = getIntent();
        startButtonTime = intent.getExtras().getLong("startButtonTime");
        taskList= intent.getExtras().getStringArrayList("taskList");

        taskDoneList= intent.getExtras().getStringArrayList("taskDoneList");
        counter = intent.getExtras().getInt("counter");
        currentIndex = intent.getExtras().getInt("currentIndex");
        taskTime= (ArrayList<Long>) intent.getSerializableExtra("taskTime");

        showIndex = intent.getExtras().getInt("showIndex");

        taskNumber = intent.getExtras().getInt("taskNumber");

        taskNumbers= intent.getExtras().getIntegerArrayList("taskNumbers");


//        System.out.println("task Number"+taskNumber);
//        System.out.println("task list"+taskList+"size"+taskList.size());
//        System.out.println("task Time"+taskTime+"size"+taskTime.size());
//        System.out.println("task Numbers"+taskNumbers+"size"+taskNumbers.size());


//        int taskNoI = currentIndex+1;
//        String taskNo = String.valueOf(taskNoI);

        String screenTitle = "Task "+String.valueOf(showIndex)+"/5 :";
        this.setTitle(screenTitle);

        currentTime = System.currentTimeMillis();
//        System.out.println("button click time"+startButtonTime);
//        System.out.println("CurrentTime"+currentTime);
//        System.out.println("diff"+(currentTime-startButtonTime));
        Toast.makeText(MainActivity.this,"diff"+(currentTime-startButtonTime),Toast.LENGTH_SHORT).show();

        CreateCharacterList();
        CreateNamesList();

        GesLib = GestureLibraries.fromRawResource(this,R.raw.gesture);
        if(!GesLib.load())
        {
//            System.out.println("Not LOADED");
            finish();
        }
        GestureOverlayView GesOver = (GestureOverlayView) findViewById(R.id.ges1);
        GesOver.addOnGesturePerformedListener(this);

        expandableListView = findViewById(R.id.e_list);
        button12 = findViewById(R.id.button12);

        expandableListAdapter = new MyExpandableListAdapter(this,Characters,Contacts);
        expandableListView.setAdapter(expandableListAdapter);

        layoutInflater = getLayoutInflater();
        ViewGroup footer = (ViewGroup) layoutInflater.inflate(R.layout.footer_layout, expandableListView, false);
        expandableListView.addFooterView(footer);

        layoutInflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) layoutInflater.inflate(R.layout.list_header, expandableListView, false);
        expandableListView.addHeaderView(header);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                    lastExpandedPosition = -1;
//                    System.out.println("group position "+groupPosition);
                    String selected = expandableListAdapter.getGroup(groupPosition).toString();
//                    Toast.makeText(MainActivity.this,selected,Toast.LENGTH_SHORT).show();
                    if(lastExpandedPosition!=-1 && groupPosition != lastExpandedPosition){
                        expandableListView.collapseGroup(lastExpandedPosition);
                    }
                    expandableListView.setSelectionFromTop(groupPosition,0);
//                    expandableListView.setSelectedGroup(groupPosition);
                    lastExpandedPosition = groupPosition;
                }
            });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    childClickTime = System.currentTimeMillis()-currentTime;
//                    System.out.println("childClickTime"+childClickTime);
                    String selected = expandableListAdapter.getChild(groupPosition,childPosition).toString();
//                    System.out.println("group position "+groupPosition+" child "+childPosition);

                    if (showIndex < 2)
                    {
//                        System.out.println("chaddi");
//                        if (groupPosition < 3 && childPosition < 2)
//                        {
//                            System.out.println("chaddi1");
//                            updateData(groupPosition,childPosition,childClickTime,taskList,taskTime,
//                                    counter,currentIndex,showIndex,taskNumbers,taskNumber,taskDoneList);
//                        }

                        if  (   (taskNumber==0 && groupPosition ==0 && childPosition == 0)||
                                (taskNumber==1 && groupPosition ==0 && childPosition == 1)||
                                (taskNumber==2 && groupPosition ==1 && childPosition == 0)||
                                (taskNumber==3 && groupPosition ==1 && childPosition == 1)||
                                (taskNumber==4 && groupPosition ==2 && childPosition == 0)
                            )
                        {
//                            System.out.println("chaddi1");
                            updateData(groupPosition,childPosition,childClickTime,taskList,taskTime,
                                    counter,currentIndex,showIndex,taskNumbers,taskNumber,taskDoneList);
                        }
                        else
                            {
                                Toast.makeText(MainActivity.this,"False Attempt",Toast.LENGTH_SHORT).show();
                                updateFalseClickData(groupPosition,childPosition,childClickTime,taskList,taskTime,
                                        counter,currentIndex,showIndex,taskNumbers,taskNumber,taskDoneList);
                            }


                    }

                    else if(showIndex==2)
                    {
                        if  (   (taskNumber==0 && groupPosition == 0 && childPosition == 0)||
                                (taskNumber==1 && groupPosition == 0 && childPosition == 1)||
                                (taskNumber==2 && groupPosition == 1 && childPosition == 0)||
                                (taskNumber==3 && groupPosition == 1 && childPosition == 1)||
                                (taskNumber==4 && groupPosition == 2 && childPosition == 0)
                        )
                        {
//                            System.out.println("33");
                            try {
                                taskTime.add(childClickTime);
                                taskDoneList.add(taskList.get(taskNumber));
//                                System.out.println("task list"+taskList+"size"+taskList.size());
//                                System.out.println("task Time"+taskTime+"size"+taskTime.size());
//                                System.out.println("done");
                                showIndex+=1;
                                counter+=1;
                                currentIndex+=1;
//                                System.out.println("cindex " + currentIndex + " counter " + counter+ " showIndex " + showIndex);
                                data.append("taskNo,time");
//                                System.out.println("kkkkkkk");
                                for(int i = 0; i<2; i++)
                                {
                                    data.append("\n").append(taskDoneList.get(i)).append(",").append(taskTime.get(i));
                                }
//                                System.out.println("kkkkkk"+data);
                                Intent intent2=new Intent(MainActivity.this,Finish.class);
                                intent2.putExtra("taskList",taskList);
                                intent2.putExtra("taskDoneList",taskDoneList);
                                intent2.putExtra("taskNumber",taskNumber);
                                intent2.putExtra("showIndex",showIndex);
                                intent2.putExtra("counter",counter);
                                intent2.putExtra("currentIndex",currentIndex);
                                intent2.putExtra("taskTime",taskTime);
                                intent2.putExtra("taskNumbers",taskNumbers);
                                String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                                String filename = date + ".csv";
                                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
//                            File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), filename);
//                            File file = new File(getApplicationContext().getFileStreamPath(filename).getPath());
//                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+filename);
//                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+filename);
//                                System.out.println(file);
                                FileOutputStream out = new FileOutputStream(file);
                                out.write((data.toString()).getBytes());
                                out.close();
//                                System.out.println("789");
                                Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                intent2.putExtra("fileLocation", file.getAbsolutePath());
                                startActivity(intent2);
                            } catch (FileNotFoundException e) {
//                                System.out.println("123");
                                e.printStackTrace();
                            } catch (IOException e) {
//                                System.out.println("456");
                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"False Attempt",Toast.LENGTH_SHORT).show();
                        }


                    }
                    return true;
                }
            });

        button12.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(),new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Toast.makeText(MainActivity.this,"Double tap",Toast.LENGTH_SHORT).show();
                    return super.onDoubleTap(e);
                }
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    if (lastExpandedPosition!=-1){
                        for(int i=0;i<26;i++)
                        {
//                            System.out.println(i);
                            expandableListView.collapseGroup(i);
                        }
                        expandableListView.collapseGroup(lastExpandedPosition);
                        Toast.makeText(MainActivity.this,"Collapsed",Toast.LENGTH_SHORT).show();
                    }
                    return super.onSingleTapConfirmed(e);
                }
            });
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
//        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Toast.makeText(MainActivity.this,"long item",Toast.LENGTH_SHORT).show();
//
//                return true;
//            }
//        });

    }

    private void updateFalseClickData(int groupPosition, int childPosition, long childClickTime, ArrayList<String> taskList, ArrayList<Long> taskTime, int counter, int currentIndex, int showIndex, ArrayList<Integer> taskNumbers, int taskNumber, ArrayList<String> taskDoneList) {
//        System.out.println("Wrong Click");
    }

    public void updateData(int groupPosition, int childPosition, long childClickTime, ArrayList<String> taskList, ArrayList<Long> taskTime, int counter, int currentIndex, int showIndex, ArrayList<Integer> taskNumbers, int taskNumber, ArrayList<String> taskDoneList)
    {

        taskTime.add(childClickTime);
        taskDoneList.add(taskList.get(taskNumber));
//        System.out.println("task list" + taskList + "size" + taskList.size());
//        System.out.println("task Time" + taskTime + "size" + taskTime.size());
//        System.out.println("task Done List" + taskDoneList + "size" + taskDoneList.size());
//        System.out.println("done");
        counter += 1;
        currentIndex += 1;
        showIndex += 1;
//        System.out.println("cindex " + currentIndex + " counter " + counter+ " showIndex " + showIndex);
        Intent intent1 = new Intent(MainActivity.this, StartScreen.class);
        intent1.putExtra("taskList", taskList);
        intent1.putExtra("counter", counter);
        intent1.putExtra("showIndex", showIndex);
        intent1.putExtra("currentIndex", currentIndex);
        intent1.putExtra("taskTime", taskTime);
        intent1.putExtra("taskNumbers", taskNumbers);
        intent1.putExtra("taskNumber", taskNumber);
        intent1.putExtra("taskDoneList", taskDoneList);
        startActivity(intent1);
    }

    @Override
    public void onBackPressed() {
    }

    private void CreateNamesList() {
        String[] contacts_a = {
                "Aaden",
                "Aaliyah",
                "Aarav",
                "Aaron",
                "Abhi",
                "Abagail",
                "Abb",
                "Abbey",
                "Abbie",
                "Abbigail",
                "Abbott",
                "Abby",
                "Abdiel",
                "Abdul",
                "Abdullah",
                "Abe",
                "Abel",
                "Abelardo",
                "Abie",
                "Abigail",
                "Abigale",
                "Abigayle",
                "Abner",
                "Abraham",
                "Abram",
                "Aedan",
                "Affie",
                "Afton",
                "Agatha",
                "Aggie",
                "Agnes",
                "Agness",
                "Agusta",
                "Alanna",
                "Alannah",
                "Alanzo",
                "Alayna",
                "Armando",
                "Armani",
                "Asha",
                "Ashanti",
                "Ashby",
                "Ashely",
                "Asher",
                "Ava",
                "Avah",
                "Averi",
                "Averie",
                "Avery",
                "Avie",
                "Avis",
                "Avon",
                "Axel",
                "Ayaan",
                "Ayana",
                "Ayanna",
                "Aydan",
                "Ayden",
                "Aydin",
                "Ayesha",
                "Ayla",
                "Aylin",
                "Azalee",
                "Azaria",
                "Azul"
        };
        String[] contacts_b = {
                "Bailee",
                "Bailey",
                "Baker",
                "Baldwin",
                "Ballard",
                "Beadie",
                "Beatrice",
                "Beatrix",
                "Beatriz",
                "Beau",
                "Beaulah",
                "Bebe",
                "Beckett",
                "Bilal",
                "Bill",
                "Billie",
                "Blain",
                "Blaine",
                "Blair",
                "Blaise",
                "Blake",
                "Blanca",
                "Blanch",
                "Blanchard",
                "Blanche",
                "Blanchie",
                "Blane",
                "Blas",
                "Blaze",
                "Bliss",
                "Blossom",
                "Bluford",
                "Bob",
                "Bobbi",
                "Bulah",
                "Buna",
                "Bunk",
                "Burdette",
                "Buren",
                "Burgess",
                "Burk",
                "Burke",
                "Burl",
                "Burleigh",
                "Burley",
                "Burnell",
                "Burnett",
                "Burney",
                "Burnice"
        };
        String[] contacts_c = {
                "Carey",
                "Cari",
                "Carie",
                "Carin",
                "Carole",
                "Carolee",
                "Carolina",
                "Carson",
                "Carter",
                "Cary",
                "Casimer",
                "Ceasar",
                "Cecelia",
                "Cecil",
                "Cecile",
                "Cecilia",
                "Cecily",
                "Cedric",
                "Cedrick",
                "Ceil",
                "Chace",
                "Chad",
                "Chadd",
                "Chadrick",
                "Chadwick",
                "Chaim",
                "Chaka",
                "Chalmer",
                "Charlize",
                "Charlotta",
                "Charlotte",
                "Charlottie",
                "Cherise",
                "Cheyenne",
                "Christie",
                "Christin",
                "Ciarra",
                "Cicely",
                "Cicero",
                "Cielo",
                "Clarabelle",
                "Clarance",
                "Clare",
                "Clarence",
                "Claribel",
                "Clay",
                "Clayton",
                "Clearence",
                "Cleave",
                "Cleda",
                "Clell",
                "Clella",
                "Clem",
                "Clemence",
                "Cleo",
                "Cleola",
                "Cleon",
                "Cleone",
                "Cleora",
                "Cleta",
                "Clora",
                "Clotilda",
                "Clotilde",
                "Clovis",
                "Cloyd",
                "Clyda",
                "Clyde",
                "Clydie",
                "Clytie",
                "Coby",
                "Codey",
                "Codi",
                "Codie",
                "Cody",
                "Coen",
                "Cohen",
                "Colbert",
                "Colby",
                "Cole",
                "Coleen",
                "Coleman",
                "Coleton",
                "Coletta",
                "Colette",
                "Coley",
                "Colie",
                "Colin",
                "Colleen",
                "Collette",
                "Collie",
                "Collier",
                "Collin",
                "Collins",
                "Collis",
                "Colon",
                "Colonel",
                "Colt",
                "Colten",
                "Colter",
                "Colton",
                "Columbia",
                "Columbus",
                "Colvin",
                "Commodore",
                "Con",
                "Conard",
                "Concepcion",
                "Concetta",
                "Concha",
                "Conley",
                "Conner",
                "Connie",
                "Connor",
                "Conor",
                "Conrad",
                "Constance",
                "Constantine",
                "Consuela",
                "Consuelo",
                "Contina",
                "Conway",
                "Coolidge",
                "Cooper",
                "Cora",
                "Coraima",
                "Coral",
                "Coralie",
                "Corbett",
                "Corbin",
                "Corda",
                "Cordaro",
                "Cordelia",
                "Cordell",
                "Cordella",
                "Cordero",
                "Cordia",
                "Cordie",
                "Corean",
                "Corene",
                "Coretta",
                "Corey",
                "Cyntha",
                "Cynthia",
                "Cyril",
                "Cyrus"

        };
        String[] contacts_d = {
                "Daisey",
                "Daisha",
                "Daisie",
                "Daisy",
                "Daisye",
                "Daja",
                "Dakoda",
                "Dakota",
                "Dakotah",
                "Dale",
                "Dalia",
                "Dallas",
                "Dallin",
                "Dalton",
                "Dalvin",
                "Damarcus",
                "Damari",
                "Damarion",
                "Damaris",
                "Dameon",
                "Damian",
                "Damien",
                "Damion",
                "Damon",
                "Damond",
                "Dan",
                "Dana",
                "Danae",
                "Dandre",
                "Dane",
                "Daneen",
                "Danelle",
                "Danette",
                "Dangelo",
                "Dani",
                "Dania",
                "Danial",
                "Danica",
                "Daniel",
                "Daniela",
                "Daniele",
                "Daniella",
                "Danielle",
                "Deacon",
                "Dean",
                "Deana",
                "Deandra",
                "Deandre",
                "Deane",
                "Deangelo",
                "Deann",
                "Deanna",
                "Deanne",
                "Deante",
                "Deasia",
                "Deb",
                "Debbi",
                "Debbie",
                "Debbra",
                "Debby",
                "Debera",
                "Debi",
                "Debora",
                "Deborah",
                "Deborrah",
                "Debra",
                "Denzell",
                "Denzil",
                "Deon",
                "Deondre",
                "Deonta",
                "Deontae",
                "Deonte",
                "Dequan",
                "Derald",
                "Dereck",
                "Derek",
                "Dereon",
                "Deric",
                "Derick",
                "Derik",
                "Desmond",
                "Dessa",
                "Dessie",
                "Destany",
                "Destin",
                "Destinee",
                "Destiney",
                "Destini",
                "Destiny",
                "Destry",
                "Devan",
                "Devante",
                "Devaughn",
                "Deven",
                "Devin",
                "Devon",
                "Devonta",
                "Devontae",
                "Devonte",
                "Devyn",
                "Deward",
                "Dewayne",
                "Dewey",
                "Dewitt",
                "Dexter",
                "Deyanira",
                "Dezzie",
                "Diallo",
                "Diamond",
                "Dian",
                "Diana",
                "Diandra",
                "Diane",
                "Diann",
                "Dianna",
                "Dianne",
                "Dicie",
                "Dick",
                "Dickie",
                "Dimitri",
                "Dimitrios",
                "Dimple",
                "Dina",
                "Dinah",
                "Dink",
                "Dino",
                "Dion",
                "Dione",
                "Doloris",
                "Dolph",
                "Dolphus",
                "Domenic",
                "Domenica",
                "Domenick",
                "Domenico",
                "Dominga",
                "Domingo",
                "Dominic",
                "Dominick",
                "Dominik",
                "Dove",
                "Dovie",
                "Dow",
                "Doyle",
                "Drusilla",
                "Duane",
                "Duard",
                "Dudley",
                "Duff",
                "Dustin",
                "Dusty",
                "Dwyane",
                "Dyan",
                "Dylan",
                "Dylon"

        };
        String[] contacts_e = {
                "Earl",
                "Earle",
                "Earlean",
                "Earlene",
                "Earley",
                "Earlie",
                "Earline",
                "Early",
                "Earnest",
                "Earnestine",
                "Eartha",
                "Easter",
                "Easton",
                "Eathel",
                "Ebb",
                "Ebba",
                "Ebbie",
                "Eben",
                "Ebenezer",
                "Eber",
                "Ebert",
                "Eboni",
                "Ebony",
                "Echo",
                "Ed",
                "Eda",
                "Edd",
                "Eddie",
                "Eddy",
                "Eden",
                "Edgar",
                "Edgardo",
                "Edie",
                "Edison",
                "Edith",
                "Edla",
                "Edmon",
                "Edmond",
                "Edmonia",
                "Edmund",
                "Edna",
                "Ednah",
                "Edra",
                "Edrie",
                "Edris",
                "Edsel",
                "Edson",
                "Eduardo",
                "Edw",
                "Edward",
                "Edwardo",
                "Edwin",
                "Edwina",
                "Edyth",
                "Edythe",
                "Effa",
                "Effie",
                "Efrain",
                "Efrem",
                "Efren",
                "Egbert",
                "Eileen",
                "Einar",
                "Eino",
                "Eithel",
                "Ela",
                "Elaina",
                "Elaine",
                "Elam",
                "Elana",
                "Elayne",
                "Elba",
                "Elbert",
                "Elberta",
                "Elbridge",
                "Elby",
                "Elda",
                "Elden",
                "Elder",
                "Eldon",
                "Eldora",
                "Eldred",
                "Eldridge",
                "Eleanor",
                "Eleanora",
                "Eleanore",
                "Elease",
                "Electa",
                "Elena",
                "Eleni",
                "Elenor",
                "Elenora",
                "Elenore",
                "Eleonora",
                "Eleonore",
                "Elex",
                "Elfie",
                "Elfreda",
                "Elfrieda",
                "Elgie",
                "Elgin",
                "Eli",
                "Elia",
                "Elian",
                "Eliana",
                "Elianna",
                "Elias",
                "Elick",
                "Elida",
                "Elie",
                "Eliezer",
                "Eliga",
                "Eligah",
                "Elige",
                "Elihu",
                "Elijah",
                "Elinor",
                "Elinore",
                "Eliot",
                "Elisa",
                "Elisabeth",
                "Elise",
                "Eliseo",
                "Elisha",
                "Elissa",
                "Eliza",
                "Elizabet",
                "Elizabeth",
                "Elizah",
                "Elizbeth",
                "Elizebeth",
                "Ell",
                "Ella",
                "Ellamae",
                "Ellar",
                "Elle",
                "Ellen",
                "Eller",
                "Ellery",
                "Elliana",
                "Ellie",
                "Elliot",
                "Elliott",
                "Ellis",
                "Ellison",
                "Ellsworth",
                "Ellwood",
                "Ellyn",
                "Elma",
                "Elmer",
                "Elmina",
                "Elmira",
                "Elmire",
                "Elmo",
                "Elmore",
                "Elmyra",
                "Elna",
                "Elnora",
                "Elodie",
                "Elois",
                "Eloisa",
                "Eloise",
                "Elon",
                "Elonzo",
                "Elouise",
                "Eloy",
                "Elroy",
                "Elsa",
                "Else",
                "Elsie",
                "Elsworth",
                "Elta",
                "Elton",
                "Elva",
                "Elvera",
                "Elvia",
                "Elvie",
                "Elvin",
                "Elvina",
                "Elvira",
                "Elvis",
                "Elwanda",
                "Elwin",
                "Elwood",
                "Elwyn",
                "Ely",
                "Elyse",
                "Elyssa",
                "Elza",
                "Elzada",
                "Elzie",
                "Elzy",
                "Ema",
                "Emaline",
                "Emanuel",
                "Emelia",
                "Emelie",
                "Emeline",
                "Emely",
                "Emerald",
                "Emerson",
                "Emery",
                "Emett",
                "Emil",
                "Emile",
                "Emilee",
                "Emilia",
                "Emiliano",
                "Emilie",
                "Emilio",
                "Emily",
                "Emit",
                "Emma",
                "Emmalee",
                "Emmaline",
                "Emmanuel",
                "Emmer",
                "Emmet",
                "Emmett",
                "Emmie",
                "Emmit",
                "Emmitt",
                "Emmons",
                "Emmy",
                "Emogene",
                "Emory",
                "Emry",
                "Ena",
                "Encarnacion",
                "Enid",
                "Ennis",
                "Enoch",
                "Enola",
                "Enos",
                "Enrico",
                "Enrique",
                "Enriqueta",
                "Enzo",
                "Eola",
                "Ephraim",
                "Ephram",
                "Ephriam",
                "Epifanio",
                "Eppie",
                "Epsie",
                "Era",
                "Erasmo",
                "Erasmus",
                "Erastus",
                "Erby",
                "Eric",
                "Erica",
                "Erich",
                "Erick",
                "Ericka",
                "Erie",
                "Erik",
                "Erika",
                "Erin",
                "Eris",
                "Erla",
                "Erland",
                "Erle",
                "Erlene",
                "Erlinda",
                "Erline",
                "Erling",
                "Erma",
                "Ermina",
                "Ermine",
                "Erna",
                "Ernest",
                "Ernestina",
                "Ernestine",
                "Ernesto",
                "Ernie",
                "Ernst",
                "Errol",
                "Ervin",
                "Erving",
                "Erwin",
                "Erykah",
                "Eryn",
                "Esau",
                "Esco",
                "Esequiel",
                "Esker",
                "Esley",
                "Esmeralda",
                "Esperanza",
                "Essa",
                "Essence",
                "Essex",
                "Essie",
                "Esta",
                "Esteban",
                "Estefani",
                "Estefania",
                "Estefany",
                "Estel",
                "Estela",
                "Estell",
                "Estella",
                "Estelle",
                "Ester",
                "Estes",
                "Estevan",
                "Esther",
                "Estie",
                "Estill",
                "Eston",
                "Estrella",
                "Etha",
                "Ethan",
                "Ethel",
                "Ethelbert",
                "Ethelene",
                "Ethelyn",
                "Ethen",
                "Ether",
                "Ethie",
                "Ethyl",
                "Ethyle",
                "Etna",
                "Etta",
                "Etter",
                "Ettie",
                "Eudora",
                "Eugene",
                "Eugenia",
                "Eugenie",
                "Eugenio",
                "Eula",
                "Eulah",
                "Eulalia",
                "Eulalie",
                "Euna",
                "Eunice",
                "Euphemia",
                "Eura",
                "Eusebio",
                "Eustace",
                "Eva",
                "Evalena",
                "Evaline",
                "Evalyn",
                "Evan",
                "Evander",
                "Evangelina",
                "Evangeline",
                "Evans",
                "Eve",
                "Evelena",
                "Evelin",
                "Evelina",
                "Eveline",
                "Evelyn",
                "Evelyne",
                "Ever",
                "Everet",
                "Everett",
                "Everette",
                "Evert",
                "Evertt",
                "Evette",
                "Evia",
                "Evie",
                "Evita",
                "Evon",
                "Evonne",
                "Ewald",
                "Ewart",
                "Ewell",
                "Ewin",
                "Ewing",
                "Exa",
                "Exie",
                "Ezekiel",
                "Ezell",
                "Ezequiel",
                "Ezra",
                "Ezzard",

        };
        String[] contacts_f = {
                "Fabian",
                "Fabiola",
                "Fae",
                "Fairy",
                "Faith",
                "Fallon",
                "Falon",
                "Fannie",
                "Fanny",
                "Fannye",
                "Farah",
                "Faron",
                "Farrah",
                "Farrell",
                "Farris",
                "Fate",
                "Fatima",
                "Faustino",
                "Fawn",
                "Fay",
                "Faye",
                "Fayette",
                "Fed",
                "Federico",
                "Felecia",
                "Felice",
                "Felicia",
                "Felicie",
                "Felicitas",
                "Felicity",
                "Felipa",
                "Felipe",
                "Felisha",
                "Felix",
                "Felton",
                "Fenton",
                "Ferd",
                "Ferdinand",
                "Ferman",
                "Fern",
                "Fernand",
                "Fernanda",
                "Fernando",
                "Ferne",
                "Ferrell",
                "Ferris",
                "Festus",
                "Fidel",
                "Fidelia",
                "Fidencio",
                "Fielding",
                "Filomena",
                "Finis",
                "Finley",
                "Finn",
                "Finnegan",
                "Fiona",
                "Firman",
                "Fisher",
                "Fitzgerald",
                "Fitzhugh",
                "Flavia",
                "Fleda",
                "Fleet",
                "Fleeta",
                "Flem",
                "Fleming",
                "Fleta",
                "Fletcher",
                "Flint",
                "Flo",
                "Flonnie",
                "Flor",
                "Flora",
                "Florance",
                "Florence",
                "Florencio",
                "Florene",
                "Florentino",
                "Floretta",
                "Florian",
                "Florida",
                "Florie",
                "Florine",
                "Florrie",
                "Flossie",
                "Floy",
                "Floyd",
                "Foch",
                "Fonda",
                "Ford",
                "Forest",
                "Forrest",
                "Foster",
                "Fount",
                "Foy",
                "Fran",
                "Franc",
                "Frances",
                "Francesca",
                "Francesco",
                "Francies",
                "Francina",
                "Francine",
                "Francis",
                "Francisca",
                "Francisco",
                "Francisqui",
                "Franco",
                "Frank",
                "Frankie",
                "Franklin",
                "Franklyn",
                "Franz",
                "Frazier",
                "Fred",
                "Freda",
                "Freddie",
                "Freddy",
                "Frederic",
                "Frederica",
                "Frederick",
                "Fredericka",
                "Fredie",
                "Fredric",
                "Fredrick",
                "Fredy",
                "Freeda",
                "Freeman",
                "Freida",
                "Fremont",
                "French",
                "Frida",
                "Frieda",
                "Friend",
                "Fritz",
                "Frona",
                "Fronia",
                "Fronie",
                "Fronnie",
                "Fuller",
                "Fulton",
                "Fumiko",
                "Furman"
        };
        String[] contacts_g = {
                "Gabe",
                "Gabriel",
                "Gabriela",
                "Gabriella",
                "Gabrielle",
                "Gael",
                "Gaetano",
                "Gage",
                "Gaige",
                "Gail",
                "Gaines",
                "Gaither",
                "Gale",
                "Galen",
                "Galilea",
                "Gannon",
                "Gardner",
                "Garett",
                "Garey",
                "Garfield",
                "Garland",
                "Garner",
                "Garnet",
                "Garnett",
                "Garold",
                "Garret",
                "Garrett",
                "Garrick",
                "Garrison",
                "Garry",
                "Garth",
                "Garvin",
                "Gary",
                "Gasper",
                "Gaston",
                "Gauge",
                "Gaven",
                "Gavin",
                "Gavyn",
                "Gay",
                "Gaye",
                "Gayla",
                "Gayle",
                "Gaylen",
                "Gaylene",
                "Gaylon",
                "Gaylord",
                "Gaynell",
                "Gearld",
                "Gearldine",
                "Geary",
                "Gee",
                "Gemma",
                "Gena",
                "Genaro",
                "Gene",
                "General",
                "Genesis",
                "Geneva",
                "Genevieve",
                "Genevra",
                "Genie",
                "Gennaro",
                "Gennie",
                "Geno",
                "Genoveva",
                "Geo",
                "Geoff",
                "Geoffrey",
                "Georganna",
                "George",
                "Georgeann",
                "Georgeanna",
                "Georgene",
                "Georgetta",
                "Georgette",
                "Georgia",
                "Georgiana",
                "Georgiann",
                "Georgianna",
                "Georgie",
                "Georgina",
                "Georgine",
                "Geovanni",
                "Gerald",
                "Geraldine",
                "Geraldo",
                "Geralyn",
                "Gerard",
                "Gerardo",
                "Gerda",
                "Gerhard",
                "Gerhardt",
                "Geri",
                "Germaine",
                "German",
                "Gerold",
                "Gerri",
                "Gerrit",
                "Gerry",
                "Gertha",
                "Gertie",
                "Gertrude",
                "Gia",
                "Giada",
                "Giana",
                "Giancarlo",
                "Gianna",
                "Gianni",
                "Gibson",
                "Gideon",
                "Gidget",
                "Gifford",
                "Gigi",
                "Gil",
                "Gilbert",
                "Gilberto",
                "Gilda",
                "Giles",
                "Gilford",
                "Gillian",
                "Gillie",
                "Gilman",
                "Gilmer",
                "Gilmore",
                "Gina",
                "Ginger",
                "Ginny",
                "Gino",
                "Giovani",
                "Giovanna",
                "Giovanni",
                "Giovanny",
                "Girtha",
                "Gisele",
                "Giselle",
                "Gisselle",
                "Giuliana",
                "Giuseppe",
                "Gladis",
                "Gladstone",
                "Gladyce",
                "Gladys",
                "Glen",
                "Glenda",
                "Glendon",
                "Glendora",
                "Glenn",
                "Glenna",
                "Glennie",
                "Glennis",
                "Glenwood",
                "Glinda",
                "Gloria",
                "Glover",
                "Glynda",
                "Glynis",
                "Glynn",
                "Godfrey",
                "Goebel",
                "Golda",
                "Golden",
                "Goldia",
                "Goldie",
                "Gonzalo",
                "Gorden",
                "Gordon",
                "Gorge",
                "Gottlieb",
                "Governor",
                "Grace",
                "Gracelyn",
                "Gracia",
                "Gracie",
                "Graciela",
                "Grady",
                "Grafton",
                "Graham",
                "Grant",
                "Granville",
                "Graves",
                "Gray",
                "Grayce",
                "Graydon",
                "Grayling",
                "Grayson",
                "Grecia",
                "Green",
                "Greene",
                "Greg",
                "Gregg",
                "Greggory",
                "Gregoria",
                "Gregorio",
                "Gregory",
                "Greta",
                "Gretchen",
                "Gretta",
                "Greyson",
                "Griffin",
                "Griffith",
                "Grisel",
                "Griselda",
                "Grove",
                "Grover",
                "Guadalupe",
                "Guido",
                "Guilford",
                "Guillermo",
                "Gunda",
                "Gunnar",
                "Gunner",
                "Gurney",
                "Gus",
                "Guss",
                "Gussie",
                "Gust",
                "Gusta",
                "Gustaf",
                "Gustav",
                "Gustave",
                "Gustavo",
                "Gustavus",
                "Gustie",
                "Guthrie",
                "Guy",
                "Gwen",
                "Gwenda",
                "Gwendolyn",
                "Gwyn",
                "Gwyneth"
        };
        String[] contacts_h = {
                "Hadassah",
                "Haden",
                "Hadley",
                "Haiden",
                "Hailee",
                "Hailey",
                "Hailie",
                "Hakeem",
                "Hakim",
                "Hal",
                "Halbert",
                "Hale",
                "Haleigh",
                "Haley",
                "Hali",
                "Halie",
                "Hall",
                "Halle",
                "Halley",
                "Hallie",
                "Halsey",
                "Ham",
                "Hamilton",
                "Hamp",
                "Hampton",
                "Hamza",
                "Hana",
                "Handy",
                "Hank",
                "Hanna",
                "Hannah",
                "Hans",
                "Hansel",
                "Hansford",
                "Hanson",
                "Harden",
                "Hardie",
                "Hardin",
                "Harding",
                "Hardy",
                "Harl",
                "Harlan",
                "Harland",
                "Harlen",
                "Harlene",
                "Harley",
                "Harlie",
                "Harlon",
                "Harlow",
                "Harm",
                "Harman",
                "Harmon",
                "Harmony",
                "Harold",
                "Harper",
                "Harrell",
                "Harrie",
                "Harriet",
                "Harriett",
                "Harriette",
                "Harris",
                "Harrison",
                "Harrold",
                "Harry",
                "Hart",
                "Hartley",
                "Hartwell",
                "Haruko",
                "Harve",
                "Harvey",
                "Harvie",
                "Harvy",
                "Hasan",
                "Hasel",
                "Haskell",
                "Hassan",
                "Hassie",
                "Hattie",
                "Haven",
                "Hayden",
                "Hayes",
                "Haylee",
                "Hayleigh",
                "Hayley",
                "Haylie",
                "Hays",
                "Hayward",
                "Haywood",
                "Hazel",
                "Hazelle",
                "Hazen",
                "Hazle",
                "Heath",
                "Heather",
                "Heaven",
                "Heber",
                "Hebert",
                "Hector",
                "Hedwig",
                "Hedy",
                "Heidi",
                "Heidy",
                "Helaine",
                "Helen",
                "Helena",
                "Helene",
                "Helga",
                "Hellen",
                "Helma",
                "Helmer",
                "Helyn",
                "Hence",
                "Henderson",
                "Henery",
                "Hennie",
                "Henretta",
                "Henri",
                "Henrietta",
                "Henriette",
                "Henry",
                "Herb",
                "Herbert",
                "Heriberto",
                "Herlinda",
                "Herma",
                "Herman",
                "Hermann",
                "Hermina",
                "Hermine",
                "Herminia",
                "Hermon",
                "Hernan",
                "Herschel",
                "Hershel",
                "Hershell",
                "Hertha",
                "Hervey",
                "Hessie",
                "Hester",
                "Hettie",
                "Hetty",
                "Heyward",
                "Hezekiah",
                "Hezzie",
                "Hideo",
                "Hilah",
                "Hilario",
                "Hilary",
                "Hilbert",
                "Hilda",
                "Hildegard",
                "Hildegarde",
                "Hildred",
                "Hildur",
                "Hill",
                "Hillard",
                "Hillary",
                "Hillery",
                "Hilliard",
                "Hilma",
                "Hilmer",
                "Hilton",
                "Hiram",
                "Hiroshi",
                "Hjalmar",
                "Hjalmer",
                "Hobart",
                "Hobert",
                "Hobson",
                "Hoke",
                "Holden",
                "Holland",
                "Holli",
                "Hollie",
                "Hollis",
                "Holly",
                "Holmes",
                "Homer",
                "Honora",
                "Hoover",
                "Hope",
                "Horace",
                "Horacio",
                "Horatio",
                "Hortencia",
                "Hortense",
                "Hortensia",
                "Horton",
                "Hosea",
                "Hosie",
                "Hosteen",
                "Houston",
                "Howard",
                "Howell",
                "Hoy",
                "Hoyt",
                "Hubbard",
                "Hubert",
                "Hudson",
                "Huey",
                "Hugh",
                "Hughes",
                "Hughey",
                "Hughie",
                "Hugo",
                "Hulda",
                "Huldah",
                "Humberto",
                "Humphrey",
                "Hung",
                "Hunt",
                "Hunter",
                "Hurbert",
                "Hurley",
                "Huston",
                "Huy",
                "Hyman",
                "Hymen",
                "Hyrum"
        };
        String[] contacts_i = {
                "Ian",
                "Ibrahim",
                "Ica",
                "Icey",
                "Icie",
                "Icy",
                "Ida",
                "Idabelle",
                "Idamae",
                "Idell",
                "Idella",
                "Iesha",
                "Ieshia",
                "Ignacio",
                "Ignatius",
                "Ignatz",
                "Ike",
                "Ila",
                "Ilah",
                "Ilda",
                "Ilene",
                "Iliana",
                "Illa",
                "Illya",
                "Ilma",
                "Ilo",
                "Ilona",
                "Ima",
                "Imani",
                "Imanol",
                "Imelda",
                "Immanuel",
                "Imo",
                "Imogene",
                "Ina",
                "India",
                "Indiana",
                "Inell",
                "Ines",
                "Inez",
                "Infant",
                "Inga",
                "Ingeborg",
                "Inger",
                "Ingram",
                "Ingrid",
                "Iola",
                "Iona",
                "Ione",
                "Ira",
                "Ireland",
                "Irena",
                "Irene",
                "Iridian",
                "Irine",
                "Iris",
                "Irl",
                "Irma",
                "Irva",
                "Irven",
                "Irvin",
                "Irvine",
                "Irving",
                "Irwin",
                "Isa",
                "Isaac",
                "Isaak",
                "Isabel",
                "Isabela",
                "Isabell",
                "Isabella",
                "Isabelle",
                "Isadora",
                "Isadore",
                "Isai",
                "Isaiah",
                "Isaias",
                "Isam",
                "Isamar",
                "Ishaan",
                "Isham",
                "Ishmael",
                "Isiah",
                "Isidor",
                "Isidore",
                "Isidro",
                "Isis",
                "Isla",
                "Ismael",
                "Isobel",
                "Isom",
                "Israel",
                "Isreal",
                "Issac",
                "Itzel",
                "Iva",
                "Ivah",
                "Ivan",
                "Ivana",
                "Iver",
                "Iverson",
                "Ivette",
                "Ivey",
                "Ivie",
                "Ivonne",
                "Ivor",
                "Ivory",
                "Ivy",
                "Iyana",
                "Iyanna",
                "Iza",
                "Izabella",
                "Izabelle",
                "Izaiah",
                "Izayah",
                "Izetta",
                "Izola",
                "Izora"


        };
        String[] contacts_j = {
                "Jabari",
                "Jabbar",
                "Jabez",
                "Jacalyn",
                "Jace",
                "Jacey",
                "Jack",
                "Jackeline",
                "Jacki",
                "Jackie",
                "Jacklyn",
                "Jackson",
                "Jacky",
                "Jaclyn",
                "Jacob",
                "Jacoby",
                "Jacque",
                "Jacquelin",
                "Jacqueline",
                "Jacquelyn",
                "Jacquelynn",
                "Jacques",
                "Jacquez",
                "Jacquline",
                "Jacqulyn",
                "Jada",
                "Jade",
                "Jaden",
                "Jadiel",
                "Jadon",
                "Jadyn",
                "Jaeda",
                "Jaeden",
                "Jaelyn",
                "Jaelynn",
                "Jagger",
                "Jaheem",
                "Jaheim",
                "Jahiem",
                "Jahir",
                "Jaida",
                "Jaiden",
                "Jaidyn",
                "Jailene",
                "Jailyn",
                "Jaime",
                "Jaimee",
                "Jaimie",
                "Jair",
                "Jairo",
                "Jajuan",
                "Jakayla",
                "Jake",
                "Jakob",
                "Jakobe",
                "Jaleel",
                "Jaleesa",
                "Jalen",
                "Jalisa",
                "Jalissa",
                "Jaliyah",
                "Jalon",
                "Jalyn",
                "Jalynn",
                "Jamaal",
                "Jamal",
                "Jamar",
                "Jamarcus",
                "Jamari",
                "Jamarion",
                "Jame",
                "Jameel",
                "Jamel",
                "James",
                "Jameson",
                "Jamey",
                "Jami",
                "Jamie",
                "Jamil",
                "Jamila",
                "Jamin",
                "Jamir",
                "Jamison",
                "Jamiya",
                "Jammie",
                "Jamya",
                "Jan",
                "Jana",
                "Janae",
                "Janay",
                "Jane",
                "Janeen",
                "Janel",
                "Janell",
                "Janelle",
                "Janene",
                "Janessa",
                "Janet",
                "Janette",
                "Janey",
                "Janiah",
                "Janice",
                "Janie",
                "Janine",
                "Janis",
                "Janiya",
                "Janiyah",
                "Jann",
                "Janna",
                "Jannette",
                "Jannie",
                "January",
                "Janyce",
                "Jaquan",
                "Jaquelin",
                "Jaqueline",
                "Jaquez",
                "Jarad",
                "Jared",
                "Jaren",
                "Jaret",
                "Jarett",
                "Jarod",
                "Jaron",
                "Jarrad",
                "Jarred",
                "Jarrell",
                "Jarret",
                "Jarrett",
                "Jarrod",
                "Jarvis",
                "Jase",
                "Jasen",
                "Jasiah",
                "Jaslene",
                "Jaslyn",
                "Jasmin",
                "Jasmine",
                "Jasmyn",
                "Jasmyne",
                "Jason",
                "Jasper",
                "Jaunita",
                "Javen",
                "Javier",
                "Javion",
                "Javon",
                "Javonte",
                "Jax",
                "Jaxon",
                "Jaxson",
                "Jay",
                "Jayce",
                "Jaycee",
                "Jaycie",
                "Jayda",
                "Jaydan",
                "Jayde",
                "Jayden",
                "Jaydin",
                "Jaydon",
                "Jaye",
                "Jayla",
                "Jaylah",
                "Jaylan",
                "Jaylee",
                "Jayleen",
                "Jaylen",
                "Jaylene",
                "Jaylin",
                "Jaylon",
                "Jaylyn",
                "Jaylynn",
                "Jayme",
                "Jaymes",
                "Jayne",
                "Jayson",
                "Jayvion",
                "Jayvon",
                "Jazlene",
                "Jazlyn",
                "Jazlynn",
                "Jazmin",
                "Jazmine",
                "Jazmyn",
                "Jazmyne",
                "Jean",
                "Jeana",
                "Jeane",
                "Jeanetta",
                "Jeanette",
                "Jeanie",
                "Jeanine",
                "Jeanmarie",
                "Jeanna",
                "Jeanne",
                "Jeannette",
                "Jeannie",
                "Jeannine",
                "Jeb",
                "Jed",
                "Jedediah",
                "Jedidiah",
                "Jeff",
                "Jefferey",
                "Jefferson",
                "Jeffery",
                "Jeffie",
                "Jeffrey",
                "Jeffry",
                "Jelani",
                "Jemal",
                "Jemima",
                "Jena",
                "Jenelle",
                "Jenifer",
                "Jenilee",
                "Jenna",
                "Jennette",
                "Jenni",
                "Jennie",
                "Jennifer",
                "Jenniffer",
                "Jennings",
                "Jenny",
                "Jens",
                "Jensen",
                "Jep",
                "Jeptha",
                "Jerad",
                "Jerald",
                "Jeraldine",
                "Jeramiah",
                "Jeramie",
                "Jeramy",
                "Jere",
                "Jered",
                "Jerel",
                "Jereme",
                "Jeremey",
                "Jeremiah",
                "Jeremie",
                "Jeremy",
                "Jeri",
                "Jerica",
                "Jerilyn",
                "Jerilynn",
                "Jerimiah",
                "Jerimy",
                "Jermain",
                "Jermaine",
                "Jermey",
                "Jerod",
                "Jerold",
                "Jerome",
                "Jeromy",
                "Jerrad",
                "Jerrel",
                "Jerrell",
                "Jerri",
                "Jerrica",
                "Jerrie",
                "Jerrilyn",
                "Jerrod",
                "Jerrold",
                "Jerry",
                "Jerusha",
                "Jeryl",
                "Jesenia",
                "Jesica",
                "Jess",
                "Jesse",
                "Jessee",
                "Jessenia",
                "Jessi",
                "Jessica",
                "Jessie",
                "Jessika",
                "Jessy",
                "Jessye",
                "Jesus",
                "Jethro",
                "Jett",
                "Jetta",
                "Jettie",
                "Jevon",
                "Jewel",
                "Jewell",
                "Jiles",
                "Jill",
                "Jillian",
                "Jim",
                "Jimena",
                "Jimmie",
                "Jimmy",
                "Jinnie",
                "Jo",
                "Joan",
                "Joana",
                "Joanie",
                "Joann",
                "Joanna",
                "Joanne",
                "Joaquin",
                "Job",
                "Jobe",
                "Jocelyn",
                "Jocelyne",
                "Jocelynn",
                "Jodi",
                "Jodie",
                "Jody",
                "Joe",
                "Joel",
                "Joell",
                "Joella",
                "Joelle",
                "Joellen",
                "Joeseph",
                "Joesph",
                "Joetta",
                "Joette",
                "Joey",
                "Johan",
                "Johana",
                "Johanna",
                "Johannah",
                "Johathan",
                "John",
                "Johnathan",
                "Johnathon",
                "Johney",
                "Johnie",
                "Johnna",
                "Johnnie",
                "Johnny",
                "Johnpaul",
                "Johnson",
                "Johny",
                "Joi",
                "Joleen",
                "Jolene",
                "Jolette",
                "Jolie",
                "Joline",
                "Jon",
                "Jonah",
                "Jonas",
                "Jonatan",
                "Jonathan",
                "Jonathon",
                "Jonell",
                "Jones",
                "Joni",
                "Jonna",
                "Jonnie",
                "Jordan",
                "Jorden",
                "Jordi",
                "Jordin",
                "Jordon",
                "Jordy",
                "Jordyn",
                "Joretta",
                "Jorge",
                "Jorja",
                "Jory",
                "Jose",
                "Josef",
                "Josefa",
                "Josefina",
                "Josefita",
                "Joselin",
                "Joseline",
                "Joseluis",
                "Joselyn",
                "Joseph",
                "Josephine",
                "Josephus",
                "Josette",
                "Josh",
                "Joshua",
                "Joshuah",
                "Josiah",
                "Josie",
                "Josiephine",
                "Joslyn",
                "Jossie",
                "Josue",
                "Journey",
                "Jovan",
                "Jovani",
                "Jovanni",
                "Jovanny",
                "Jovany",
                "Jovita",
                "Joy",
                "Joyce",
                "Joycelyn",
                "Joye",
                "Juan",
                "Juana",
                "Juanita",
                "Judah",
                "Judd",
                "Jude",
                "Judge",
                "Judi",
                "Judie",
                "Judith",
                "Judson",
                "Judy",
                "Judyth",
                "Jule",
                "Jules",
                "Juli",
                "Julia",
                "Julian",
                "Juliana",
                "Juliann",
                "Julianna",
                "Julianne",
                "Julie",
                "Julien",
                "Juliet",
                "Juliette",
                "Julio",
                "Julious",
                "Julisa",
                "Julissa",
                "Julius",
                "Juluis",
                "June",
                "Junia",
                "Junie",
                "Junior",
                "Junious",
                "Junius",
                "Justen",
                "Justice",
                "Justin",
                "Justina",
                "Justine",
                "Juston",
                "Justus",
                "Justyn",
                "Juwan"

        };
        String[] contacts_k = {

        };
        String[] contacts_l = {

        };
        String[] contacts_m = {

        };
        String[] contacts_n = {

        };
        String[] contacts_o = {

        };
        String[] contacts_p = {

        };
        String[] contacts_q = {

        };
        String[] contacts_r = {

        };
        String[] contacts_s = {

        };
        String[] contacts_t = {

        };
        String[] contacts_u = {

        };
        String[] contacts_v = {

        };
        String[] contacts_w = {

        };
        String[] contacts_x = {

        };
        String[] contacts_y = {

        };
        String[] contacts_z = {

        };




        Contacts = new HashMap<String, List<String>>();
        for(String characters : Characters){
            if(characters.equals("A")){
                LoadNames(contacts_a);
            }
            else if(characters.equals("B")){
                LoadNames(contacts_b);
            }
            else if(characters.equals("C")){
                LoadNames(contacts_c);
            }
            else if(characters.equals("D")){
                LoadNames(contacts_d);
            }
            else if(characters.equals("E")){
                LoadNames(contacts_e);
            }
            else if(characters.equals("F")){
                LoadNames(contacts_f);
            }
            else if(characters.equals("G")){
                LoadNames(contacts_g);
            }
            else if(characters.equals("H")){
                LoadNames(contacts_h);
            }
            else if(characters.equals("I")){
                LoadNames(contacts_i);
            }
            else if(characters.equals("J")){
                LoadNames(contacts_j);
            }
            else if(characters.equals("K")){
                LoadNames(contacts_k);
            }
            else if(characters.equals("L")){
                LoadNames(contacts_l);
            }
            else if(characters.equals("M")){
                LoadNames(contacts_m);
            }
            else if(characters.equals("N")){
                LoadNames(contacts_n);
            }
            else if(characters.equals("O")){
                LoadNames(contacts_o);
            }
            else if(characters.equals("P")){
                LoadNames(contacts_p);
            }
            else if(characters.equals("Q")){
                LoadNames(contacts_q);
            }
            else if(characters.equals("R")){
                LoadNames(contacts_r);
            }
            else if(characters.equals("S")){
                LoadNames(contacts_s);
            }
            else if(characters.equals("T")){
                LoadNames(contacts_t);
            }
            else if(characters.equals("U")){
                LoadNames(contacts_u);
            }
            else if(characters.equals("V")){
                LoadNames(contacts_v);
            }
            else if(characters.equals("W")){
                LoadNames(contacts_w);
            }
            else if(characters.equals("X")){
                LoadNames(contacts_x);
            }
            else if(characters.equals("Y")){
                LoadNames(contacts_y);
            }
            else if(characters.equals("Z")){
                LoadNames(contacts_z);
            }
            Contacts.put(characters,Names);
        }

    }

    private void LoadNames(String[] character_contacts) {
        Names = new ArrayList<>();
        for(String names: character_contacts){
            String characterThird = String.valueOf(names.charAt(2)).toLowerCase();
            String characterTwo = String.valueOf(names.charAt(1)).toLowerCase();
            String characterFirst = String.valueOf(names.charAt(0)).toLowerCase();
//            System.out.println(characterFirst+"   "+characterTwo+ "  "+characterThird);
            if(secondCharacter>=1 && (lastCharacterList.equals("a")
                    || lastCharacterList.equals("b")
                    || lastCharacterList.equals("c")
                    || lastCharacterList.equals("d")
                    || lastCharacterList.equals("e")
                    || lastCharacterList.equals("f")
                    || lastCharacterList.equals("g")
                    || lastCharacterList.equals("h")
                    || lastCharacterList.equals("i")
                    || lastCharacterList.equals("j")
                    || lastCharacterList.equals("k")
                    || lastCharacterList.equals("l")
                    || lastCharacterList.equals("m")
                    || lastCharacterList.equals("n")
                    || lastCharacterList.equals("o")
                    || lastCharacterList.equals("p")
                    || lastCharacterList.equals("q")
                    || lastCharacterList.equals("r")
                    || lastCharacterList.equals("s")
                    || lastCharacterList.equals("t")
                    || lastCharacterList.equals("u")
                    || lastCharacterList.equals("v")
                    || lastCharacterList.equals("w")
                    || lastCharacterList.equals("x")
                    || lastCharacterList.equals("y")
                    || lastCharacterList.equals("z")

            ))
            {
                if(secondCharacter==1)
                {
//                    if(characterTwo.equals("a") && currentPredict.equals("a"))
                    if(characterTwo.equals(currentPredict))
                    {
                        System.out.println(names);
                        System.out.println("&");
//                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
                        Names.add(names);
                    }
//                    else if(characterTwo.equals("b") && currentPredict.equals("b"))
//                    {
//                        System.out.println("&&");
////                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
//                        Names.add(names);
//                    }
//                    else if(characterTwo.equals("c") && currentPredict.equals("c"))
//                    {
//                        System.out.println("&&&");
////                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
//                        Names.add(names);
//                    }
//                    else if(characterTwo.equals("d") && currentPredict.equals("d"))
//                    {
//                        System.out.println("&&&&");
////                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
//                        Names.add(names);
//                    }
                }
                else if(secondCharacter==2 && characterTwo.equals(previousCharacter))
                {
                    System.out.println(names);
                    if(characterThird.equals(currentPredict))
                    {
                        System.out.println("&");
//                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
                        Names.add(names);
                    }
//                    if(characterThird.equals("a") && currentPredict.equals("a") )
//                    {
//                        System.out.println("&");
////                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
//                        Names.add(names);
//                    }
//                    else if(characterThird.equals("b") && currentPredict.equals("b"))
//                    {
//                        System.out.println("&&");
////                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
//                        Names.add(names);
//                    }
//                    else if(characterThird.equals("c") && currentPredict.equals("c"))
//                    {
//                        System.out.println("&&&");
////                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
//                        Names.add(names);
//                    }
//                    else if(characterThird.equals("d") && currentPredict.equals("d"))
//                    {
//                        System.out.println("&&&&");
////                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
//                        Names.add(names);
//                    }
                }
            }

            else
            {
                Names.add(names);
            }
        }
    }
    private void CreateCharacterList() {
        Characters = new ArrayList<>();
        Characters.add("A");
        Characters.add("B");
        Characters.add("C");
        Characters.add("D");
        Characters.add("E");
        Characters.add("F");
        Characters.add("G");
        Characters.add("H");
        Characters.add("I");
        Characters.add("J");
        Characters.add("K");
        Characters.add("L");
        Characters.add("M");
        Characters.add("N");
        Characters.add("O");
        Characters.add("P");
        Characters.add("Q");
        Characters.add("R");
        Characters.add("S");
        Characters.add("T");
        Characters.add("U");
        Characters.add("V");
        Characters.add("W");
        Characters.add("X");
        Characters.add("Y");
        Characters.add("Z");
    }
    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {

        ArrayList<Prediction> ObjPrediction = GesLib.recognize(gesture);
        if(ObjPrediction.size()>0 && ObjPrediction.get(0).score>1)
        {
            for(int i=0;i<26;i++)
            {
//                System.out.println(i);
                expandableListView.collapseGroup(i);
            }

            if(secondCharacter>=1 && secondCharacter<3)
            {
//                System.out.println("");
//                System.out.println(ObjPrediction);
                predict = ObjPrediction.get(0).name;
                predict = predict.toString().toLowerCase();
//                System.out.println("predict  in second"+predict);
                drawCharacterList.add(predict);
                System.out.println(drawCharacterList);
                if(secondCharacter==2)
                {
                   previousCharacter=drawCharacterList.get(1);
//                    System.out.println("lastlist "+lastCharacterList+lastCharacterPosition+previousCharacter+predict);
                }

                lastCharacterList = drawCharacterList.get(0);
                lastCharacterPosition = drawCharacterPosition.get(0);
                currentPredict=predict;

                CreateCharacterList();
                CreateNamesList();
                secondCharacter +=1;
                expandableListAdapter = new MyExpandableListAdapter(this,Characters,Contacts);
                expandableListView.setAdapter(expandableListAdapter);

                if(predict.equals("a"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("b"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("c"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("e"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("f"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("g"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("h"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("i"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("j"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("k"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("l"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("m"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("n"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("o"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("p"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("q"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("r"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("s"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("t"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("u"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("v"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("w"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("x"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("y"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("z"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }

            }
            else if(secondCharacter>=3)
            {
                secondCharacter=0;
                drawCharacterPosition.clear();
                drawCharacterList.clear();
                lastCharacterList=null;
                predict = ObjPrediction.get(0).name;
                predict = predict.toString().toLowerCase();
                drawCharacterList.add(predict);
//                secondCharacter+=1;
                currentPredict=predict;
                CreateCharacterList();
                CreateNamesList();
                secondCharacter +=1;
                expandableListAdapter = new MyExpandableListAdapter(this,Characters,Contacts);
                expandableListView.setAdapter(expandableListAdapter);

                if(predict.equals("a"))
                {
                    drawCharacterPosition.add(0);
                    expandableListView.expandGroup(0);
                }
                else if(predict.equals("b"))
                {
                    drawCharacterPosition.add(1);
                    expandableListView.expandGroup(1);
                }
                else if(predict.equals("c"))
                {
                    drawCharacterPosition.add(2);
                    expandableListView.expandGroup(2);
                }
                else if(predict.equals("d"))
                {
                    drawCharacterPosition.add(3);
                    expandableListView.expandGroup(3);
                }
                else if(predict.equals("e"))
                {
                    drawCharacterPosition.add(4);
                    expandableListView.expandGroup(4);
                }
                else if(predict.equals("f"))
                {
                    drawCharacterPosition.add(5);
                    expandableListView.expandGroup(5);
                }
                else if(predict.equals("g"))
                {
                    drawCharacterPosition.add(6);
                    expandableListView.expandGroup(6);
                }
                else if(predict.equals("h"))
                {
                    drawCharacterPosition.add(7);
                    expandableListView.expandGroup(7);
                }
                else if(predict.equals("i"))
                {
                    drawCharacterPosition.add(8);
                    expandableListView.expandGroup(8);
                }
                else if(predict.equals("j"))
                {
                    drawCharacterPosition.add(9);
                    expandableListView.expandGroup(9);
                }
                else if(predict.equals("k"))
                {
                    drawCharacterPosition.add(10);
                    expandableListView.expandGroup(10);
                }
                else if(predict.equals("l"))
                {
                    drawCharacterPosition.add(11);
                    expandableListView.expandGroup(11);
                }
                else if(predict.equals("m"))
                {
                    drawCharacterPosition.add(12);
                    expandableListView.expandGroup(12);
                }
                else if(predict.equals("n"))
                {
                    drawCharacterPosition.add(13);
                    expandableListView.expandGroup(13);
                }
                else if(predict.equals("o"))
                {
                    drawCharacterPosition.add(14);
                    expandableListView.expandGroup(14);
                }
                else if(predict.equals("p"))
                {
                    drawCharacterPosition.add(15);
                    expandableListView.expandGroup(15);
                }
                else if(predict.equals("q"))
                {
                    drawCharacterPosition.add(16);
                    expandableListView.expandGroup(16);
                }
                else if(predict.equals("r"))
                {
                    drawCharacterPosition.add(17);
                    expandableListView.expandGroup(17);
                }
                else if(predict.equals("s"))
                {
                    drawCharacterPosition.add(18);
                    expandableListView.expandGroup(18);
                }
                else if(predict.equals("t"))
                {
                    drawCharacterPosition.add(19);
                    expandableListView.expandGroup(19);
                }
                else if(predict.equals("u"))
                {
                    drawCharacterPosition.add(20);
                    expandableListView.expandGroup(20);
                }
                else if(predict.equals("v"))
                {
                    drawCharacterPosition.add(21);
                    expandableListView.expandGroup(21);
                }
                else if(predict.equals("w"))
                {
                    drawCharacterPosition.add(22);
                    expandableListView.expandGroup(22);
                }
                else if(predict.equals("x"))
                {
                    drawCharacterPosition.add(23);
                    expandableListView.expandGroup(23);
                }
                else if(predict.equals("y"))
                {
                    drawCharacterPosition.add(24);
                    expandableListView.expandGroup(24);
                }
                else if(predict.equals("z"))
                {
                    drawCharacterPosition.add(25);
                    expandableListView.expandGroup(25);
                }


            }
            else
            {

//                System.out.println(ObjPrediction);
                predict = ObjPrediction.get(0).name;
                predict = predict.toString().toLowerCase();
                drawCharacterList.add(predict);
                secondCharacter+=1;
                currentPredict=predict;
                if(predict.equals("a"))
                {
                    drawCharacterPosition.add(0);
                    expandableListView.expandGroup(0);
                }
                else if(predict.equals("b"))
                {
                    drawCharacterPosition.add(1);
                    expandableListView.expandGroup(1);
                }
                else if(predict.equals("c"))
                {
                    drawCharacterPosition.add(2);
                    expandableListView.expandGroup(2);
                }
                else if(predict.equals("d"))
                {
                    drawCharacterPosition.add(3);
                    expandableListView.expandGroup(3);
                }
                else if(predict.equals("e"))
                {
                    drawCharacterPosition.add(4);
                    expandableListView.expandGroup(4);
                }
                else if(predict.equals("f"))
                {
                    drawCharacterPosition.add(5);
                    expandableListView.expandGroup(5);
                }
                else if(predict.equals("g"))
                {
                    drawCharacterPosition.add(6);
                    expandableListView.expandGroup(6);
                }
                else if(predict.equals("h"))
                {
                    drawCharacterPosition.add(7);
                    expandableListView.expandGroup(7);
                }
                else if(predict.equals("i"))
                {
                    drawCharacterPosition.add(8);
                    expandableListView.expandGroup(8);
                }
                else if(predict.equals("j"))
                {
                    drawCharacterPosition.add(9);
                    expandableListView.expandGroup(9);
                }
                else if(predict.equals("k"))
                {
                    drawCharacterPosition.add(10);
                    expandableListView.expandGroup(10);
                }
                else if(predict.equals("l"))
                {
                    drawCharacterPosition.add(11);
                    expandableListView.expandGroup(11);
                }
                else if(predict.equals("m"))
                {
                    drawCharacterPosition.add(12);
                    expandableListView.expandGroup(12);
                }
                else if(predict.equals("n"))
                {
                    drawCharacterPosition.add(13);
                    expandableListView.expandGroup(13);
                }
                else if(predict.equals("o"))
                {
                    drawCharacterPosition.add(14);
                    expandableListView.expandGroup(14);
                }
                else if(predict.equals("p"))
                {
                    drawCharacterPosition.add(15);
                    expandableListView.expandGroup(15);
                }
                else if(predict.equals("q"))
                {
                    drawCharacterPosition.add(16);
                    expandableListView.expandGroup(16);
                }
                else if(predict.equals("r"))
                {
                    drawCharacterPosition.add(17);
                    expandableListView.expandGroup(17);
                }
                else if(predict.equals("s"))
                {
                    drawCharacterPosition.add(18);
                    expandableListView.expandGroup(18);
                }
                else if(predict.equals("t"))
                {
                    drawCharacterPosition.add(19);
                    expandableListView.expandGroup(19);
                }
                else if(predict.equals("u"))
                {
                    drawCharacterPosition.add(20);
                    expandableListView.expandGroup(20);
                }
                else if(predict.equals("v"))
                {
                    drawCharacterPosition.add(21);
                    expandableListView.expandGroup(21);
                }
                else if(predict.equals("w"))
                {
                    drawCharacterPosition.add(22);
                    expandableListView.expandGroup(22);
                }
                else if(predict.equals("x"))
                {
                    drawCharacterPosition.add(23);
                    expandableListView.expandGroup(23);
                }
                else if(predict.equals("y"))
                {
                    drawCharacterPosition.add(24);
                    expandableListView.expandGroup(24);
                }
                else if(predict.equals("z"))
                {
                    drawCharacterPosition.add(25);
                    expandableListView.expandGroup(25);
                }
                Toast.makeText(this,predict,Toast.LENGTH_SHORT).show();
            }

        }
    }
}