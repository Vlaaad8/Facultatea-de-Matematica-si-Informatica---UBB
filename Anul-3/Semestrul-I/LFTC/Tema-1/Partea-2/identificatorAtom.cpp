// #include <iostream>
// #include <fstream>
// #include <map>
// #include <cctype>
// #include <unordered_map>
// #include <vector>
// #include "header/createTS.h"
// using namespace std;
//
// ifstream in("program.txt");
// ifstream inC("constantTable.txt");
//
// ofstream out("atoms.txt");
//
// map<string, int> generateReservedVocabulary() {
//     map<string, int> res;
//     if (!inC.is_open()) {
//         cout << "Error in opening Vocabulary file" << endl;
//         exit(1);
//     }
//     string line;
//     while (getline(inC, line)) {
//         int idx = line.find(' ');
//         if (idx != string::npos) {
//             string word = line.substr(0, idx);
//             int index = stoi(line.substr(idx + 1));
//             res.insert(make_pair(word, index));
//         }
//     }
//     return res;
// }
// // string defineCategory(int codeID) {
// //     if (codeID >= 2 and codeID <= 15) {
// //         return "Operator";
// //     }
// //     if (codeID >= 16 and codeID <= 19) {
// //         return "Separator";
// //     }
// //     if (codeID >= 20 and codeID <= 35 ) {
// //         return "Keyword";
// //     }
// //     return "Unknown";
// // }
//
// int main() {
//     vector<string> toSort;
//     map<string, int> res = generateReservedVocabulary();
//     vector<pair<string, int>> atoms;
//     if (!in.is_open()) {
//         cout << "Error in opening input file" << endl;
//         return 0;
//     }
//
//     if (!out.is_open()) {
//         cout << "Error in opening output file" << endl;
//         return 0;
//     }
//     out << "Atom" << " " << "Cod Atom" << "Cod in TS" << endl;
//     string c;
//     while (getline(in, c)) {
//         string tmp = "";
//         for (int i = 0; i < c.size(); i++) {
//             if (ispunct(c[i]) && c[i] != '.') {
//                 if (!tmp.empty()) {
//                     if (res.contains(tmp)) {
//                         atoms.push_back(make_pair(tmp, res[tmp]));
//                         //out << tmp << "   ----> Cod:" << res[tmp] <<"   ----> Type:"<< defineCategory(res[tmp])<<  endl;
//                     } else {
//                         toSort.push_back(tmp);
//                         atoms.push_back(make_pair(tmp, -1));
//                         //out << tmp << " -" << endl;
//                     }
//                     tmp.clear();
//                 }
//                 if (ispunct(c[i + 1]) && i + 1 < c.size() && ispunct(c[i + 1]) && c[i+1]!=')' and c[i]!='(')  {
//                     string newSimbol = string() + c[i] + c[i + 1];
//                     if (res.contains(newSimbol)) {
//                         atoms.push_back(make_pair(newSimbol, res[newSimbol]));
//                         //out << newSimbol << "   ----> Cod:" << res[newSimbol] <<"   ----> Type:"<< defineCategory(res[newSimbol])<<  endl;
//                     } else {
//                         atoms.push_back(make_pair(newSimbol, -1));
//                         //out << newSimbol << " -" << endl;
//                     }
//                     i++;
//                 }
//                 else {
//                     if (res.contains(string(1,c[i]))) {
//                         atoms.push_back(make_pair(string(1,c[i]), res[string(1,c[i])]));
//                        // out << string(1,c[i]) << "   ----> Cod:" << res[string(1,c[i])] <<"   ----> Type:"<< defineCategory(res[string(1,c[i])])<<  endl;
//                     } else {
//                         toSort.push_back(string(1,c[i]));
//                         atoms.push_back(make_pair(string(1,c[i]), -1));
//                         //out << string(1,c[i]) << " -" << endl;
//                     }
//                 }
//
//             } else if (isspace(c[i])) {
//                 if (!tmp.empty()) {
//                     if (res.contains(tmp)) {
//                         atoms.push_back(make_pair(tmp, res[tmp]));
//                         //out << tmp <<  "   ----> Cod:" << res[tmp] <<"   ----> Type:"<< defineCategory(res[tmp])<<  endl;
//                     } else {
//                         toSort.push_back(tmp);
//                         atoms.push_back(make_pair(tmp, -1));
//                         //out << tmp << " -" << endl;
//                     }
//                     tmp.clear();
//                 }
//             } else {
//                 tmp += c[i];
//             }
//         }
//     }
//
//     generateTS(toSort);
//     map<string,int> tsID= getTSID();
//     map<string,int> tsCT= getTSCT();
//     for (pair element : atoms) {
//         if (element.second==-1) {
//             if (tsID.contains(element.first)) {
//                 out<<element.first<<"   ----> "<<"ID   ----> Cod-TS-ID:"<<tsID[element.first]<<endl;
//             }
//             else if (tsCT.contains(element.first)) {
//                 out<<element.first<<"   ----> "<<"CT   ----> Cod-TS-CT:"<<tsCT[element.first]<<endl;
//             }
//             else {
//                 out<<element.first<<"   ----> "<<"Unknown"<<endl;
//             }
//         }
//         else{
//             out<<element.first<<"   ----> Cod:"<<element.second<<"   ----> Tip:"<<defineCategory(element.second)<<endl;
//         }
//     }
//     in.close();
//     out.close();
// }
