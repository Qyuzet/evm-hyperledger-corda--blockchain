// SPDX-License-Identifier: GPL-3.0

pragma solidity >=0.4.0 <0.9.0;

/**
 * @title Storage
 * @dev Store & retrieve value in a variable
 * @custom:dev-run-script ./scripts/deploy_with_ethers.ts
 */

contract Scorecard{

    address public classTeacher;
    uint256 studentCount = 0;

    constructor(){
        classTeacher = msg.sender;
    }

    modifier onlyClassTeacher(address _classTeacher){
        require(classTeacher == _classTeacher, "Only the class teacher has access to this function");
        _;
    }

    struct studentDetails{
        string studentFirstName;
        string studentLastName;
        uint256 id;
    }

    struct Score{
        uint256 studentId;
        uint256 englishMarks;
        uint256 mathMarks;
        uint256 scienceMarks;
    
    }

    mapping (uint => studentDetails) students;

    mapping (uint => Score) scores;

    event studentAdded (string _studentFirstName , string _studentLastName, uint256 _studentId);

    event studentScoresRecorded(uint256 _studentId, uint256 _englishMarks, uint256 _mathMarks, uint256 _scienceMarks);


    function addStudentDetails(string memory _studentFirstName
                                , string memory _studentLastName) public onlyClassTeacher(msg.sender) {

        studentDetails storage studentObj = students[studentCount];

        studentObj.studentFirstName = _studentFirstName;
        studentObj.studentLastName = _studentLastName;
        studentObj.id  = studentCount;
        emit studentAdded (_studentFirstName, _studentLastName, studentCount);
        studentCount++;
    }

    function addStudentScores( uint256 _studentId,
                                uint256 _englishMarks,
                                uint256 _mathMarks,
                                uint256 _scienceMarks) public onlyClassTeacher(msg.sender){

        Score storage scoreObject = scores[_studentId];

        scoreObject.studentId = _studentId;
        scoreObject.englishMarks = _englishMarks;
        scoreObject.mathMarks = _mathMarks;
        scoreObject.scienceMarks = _scienceMarks;
        emit studentScoresRecorded( _studentId,  _englishMarks,  _mathMarks,  _scienceMarks);
    }






}