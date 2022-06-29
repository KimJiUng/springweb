let player1 = [];
let player2 = [];
let turn = 1;
let board = [
["","","","","","","","","","","","","","",""],
["","","","","","","","","","","","","","",""],
["","","","","","","","","","","","","","",""],
["","","","","","","","","","","","","","",""],
["","","","","","","","","","","","","","",""],
["","","","","","","","","","","","","","",""],
["","","","","","","","","","","","","","",""],
["","","","","","","","","","","","","","",""],
["","","","","","","","","","","","","","",""],
["","","","","","","","","","","","","","",""],
["","","","","","","","","","","","","","",""],
["","","","","","","","","","","","","","",""],
["","","","","","","","","","","","","","",""],
["","","","","","","","","","","","","","",""],
["","","","","","","","","","","","","","",""]
];
getboard();

function restart(){
      location.reload();
//    gameend = false;
//    winner = "";
//    player1 = [];
//    player2 = [];
//    board = [
//    ["","","","","","","","","","","","","","",""],
//    ["","","","","","","","","","","","","","",""],
//    ["","","","","","","","","","","","","","",""],
//    ["","","","","","","","","","","","","","",""],
//    ["","","","","","","","","","","","","","",""],
//    ["","","","","","","","","","","","","","",""],
//    ["","","","","","","","","","","","","","",""],
//    ["","","","","","","","","","","","","","",""],
//    ["","","","","","","","","","","","","","",""],
//    ["","","","","","","","","","","","","","",""],
//    ["","","","","","","","","","","","","","",""],
//    ["","","","","","","","","","","","","","",""],
//    ["","","","","","","","","","","","","","",""],
//    ["","","","","","","","","","","","","","",""],
//    ["","","","","","","","","","","","","","",""]
//    ];
//    turn = 1;
//    $("#truncheck").html("player1 차례입니다.");
//    getboard();
}

function getboard(){
    let html = '<table>';
    for(let i=0; i<15; i++){
        html += '<tr>';
        for(let j=0; j<15; j++){
            let ji= j+','+i;
            let p1check = false;
            let p2check = false;
            for(let a=0; a<player1.length; a++){
                if(player1[a]==ji){
                    p1check = true;
                }
            }
            for(let a=0; a<player2.length; a++){
                if(player2[a]==ji){
                    p2check = true;
                }
            }

            if(p1check){
                html += '<td>O</td>';
            }else if(p2check){
                html += '<td>X</td>';
            }else{
                html += '<td onclick="boardclick('+j+','+i+')"></td>';
            }
        }
        html += '</tr>';
    }
    html += '</table>';
    document.getElementById("gameboard").innerHTML = html;

}


let winner = "";
let gameend = false;
function boardclick(x,y){
    if(gameend){
        alert("더 이상 두실 수 없습니다. 게임 재시작을 눌러주세요.");
    }else{
        let xy = x+','+y;
        if(turn==1){
            player1.push(xy);
            board[x][y] = "O";
            turn = 2;
            document.getElementById("truncheck").innerHTML = "player2 차례입니다.";
            p2.style.cssText = "background-color: red";
            p1.style.cssText = "background-color: white";

        }else{
            player2.push(xy);
            board[x][y] = "X";
            turn = 1;
            document.getElementById("truncheck").innerHTML = "player1 차례입니다.";
            p1.style.cssText = "background-color: red";
            p2.style.cssText = "background-color: white";
        }
        console.log(player1);
        console.log(player2);
        console.log(board);
        getboard();

        for(let i=2; i<13; i++){
            for(let j=0; j<15; j++){
                // 세로 승리 조건
                if( board[j][(i-2)] == board[j][i] && board[j][(i-1)] == board[j][i] &&
                    board[j][(i+1)] == board[j][i] && board[j][(i+2)] == board[j][i] && board[j][i] != "" ){
                    if(board[j][i]=="O"){
                        winner = "player1";
                    }else if(board[j][i]=="X"){
                        winner = "player2";
                    }
                }
                // 가로 승리 조건
                if( board[(i-2)][j] == board[i][j] && board[(i-1)][j] == board[i][j] &&
                    board[(i+1)][j] == board[i][j] && board[(i+2)][j] == board[i][j] && board[i][j] != "" ){
                    if(board[i][j]=="O"){
                        winner = "player1";
                    }else if(board[i][j]=="X") {
                        winner = "player2";
                    }
                }
            }
        }

        for(let i=2; i<13; i++){
            for(let j=2; j<13; j++){
                if( board[j-2][i-2] == board[j][i] && board[j-1][i-1] == board[j][i] &&
                    board[j+1][i+1] == board[j][i] && board[j+2][i+2] == board[j][i] && board[j][i] != ""){
                    if(board[j][i]=="O"){
                        winner = "player1";
                    }else if(board[j][i]=="X"){
                        winner = "player2";
                    }
                }

                if( board[j+2][i-2] == board[j][i] && board[j+1][i-1] == board[j][i] &&
                    board[j-1][i+1] == board[j][i] && board[j-2][i+2] == board[j][i] && board[j][i] != ""){
                    if(board[j][i]=="O"){
                        winner = "player1";
                    }else if(board[j][i]=="X"){
                        winner = "player2";
                    }
                }


            }
        }

        if(winner!=""){
            alert(winner+"승리!");
            gameend=true;
        }
    }



}