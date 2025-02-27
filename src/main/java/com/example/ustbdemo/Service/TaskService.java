package com.example.ustbdemo.Service;

import com.example.ustbdemo.Model.DataModel.*;
import com.example.ustbdemo.Repository.*;
import com.example.ustbdemo.Util.FileUtil;
import com.example.ustbdemo.Util.GitProcess;
import com.example.ustbdemo.Util.OSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Service("taskService")
public class TaskService {
    private final AssembleChooseRepository assembleChooseRepository;
    private final AssembleChooseScoreRepository assembleChooseScoreRepository;
    private final InstructionRepository instructionRepository;
    private final QuestionRepository questionRepository;
    private final ScoreRepository scoreRepository;
    private final SimulationRepository simulationRepository;
    private final TaskQuestionRepository taskQuestionRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final StageRepository stageRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, ScoreRepository scoreRepository,
                       TaskQuestionRepository taskQuestionRepository, SimulationRepository simulationRepository,
                       InstructionRepository instructionRepository,
                       AssembleChooseRepository assembleChooseRepository,
                       AssembleChooseScoreRepository assembleChooseScoreRepository,
                       QuestionRepository questionRepository,
                       UserRepository userRepository,
                       StageRepository stageRepository){
        Assert.notNull(taskRepository, "taskRepository must not be null!");
        Assert.notNull(scoreRepository, "scoreRepository must not be null!");
        Assert.notNull(taskQuestionRepository, "taskQuestionRepository must not be null!");
        Assert.notNull(instructionRepository, "instructionRepository must not be null!");
        Assert.notNull(simulationRepository, "simulationRepository must not be null!");
        Assert.notNull(assembleChooseRepository, "assembleChooseRepository must not be null!");
        Assert.notNull(assembleChooseScoreRepository , "assembleChooseScoreRepository must not be null!");
        Assert.notNull(questionRepository , "questionRepository must not be null!");
        Assert.notNull(userRepository , "userRepository must not be null!");
        Assert.notNull(stageRepository , "stageRepository must not be null!");
        this.taskRepository = taskRepository;
        this.scoreRepository = scoreRepository;
        this.taskQuestionRepository = taskQuestionRepository;
        this.instructionRepository = instructionRepository;
        this.simulationRepository = simulationRepository;
        this.assembleChooseRepository = assembleChooseRepository;
        this.assembleChooseScoreRepository = assembleChooseScoreRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.stageRepository=stageRepository;
    }

    public List<Simulation> getAllSimulation(){
        return this.simulationRepository.findAll();
    }

    public Simulation getSimulationBySimuid(Long simuid){
        try{
            return this.simulationRepository.findById(simuid).get();
        } catch (Exception e){
            return  null;
        }
    }

    public Instruction getInstructionByInstrId(Long instrId){
        try{
            return this.instructionRepository.findById(instrId).get();
        } catch (Exception e){
            return  null;
        }
    }

    public void addSimulation(Simulation simulation){
        this.simulationRepository.save(simulation);
    }

    public void initialRepository(){
        this.assembleChooseRepository.deleteAll();
        this.instructionRepository.deleteAll();
        this.taskQuestionRepository.deleteAll();
        this.simulationRepository.deleteAll();
        this.taskRepository.deleteAll();
        this.assembleChooseScoreRepository.deleteAll();
        this.questionRepository.deleteAll();
        this.userRepository.deleteAll();
        this.scoreRepository.deleteAll();
        this.stageRepository.deleteAll();
    }

    public List<Instruction> getAllInstruction(){
        return this.instructionRepository.findAll();
    }

    public void addInstruction(Instruction instruction){
        this.instructionRepository.save(instruction);
    }

    public void saveTask(Task task) {
        this.taskRepository.save(task);
    }

    public void saveAssembleChoose(Assemble_Choose assemble_choose){
        this.assembleChooseRepository.save(assemble_choose);
    }

    public void saveInstruction(Instruction instruction){
        this.instructionRepository.save(instruction);
    }


    public List<Task> getAllTasks(){
        return this.taskRepository.findAll();
    }

    public Task getTaskByTid(Long tid){
        System.out.println(tid);
        try{
            return this.taskRepository.findById(tid).get();
        } catch (Exception e){
            return  null;
        }
    }

    public Assemble_Choose getAssembleChooseByTid(Long tcid){
        try{
            return this.assembleChooseRepository.findById(tcid).get();
        } catch (Exception e){
            return  null;
        }
    }


    public void deleteAssembleChooseByTcide(Long tcid){
        this.assembleChooseRepository.deleteById(tcid);
        Assemble_Choose_Score score = new Assemble_Choose_Score();
        score.setTcid(tcid);
        Example<Assemble_Choose_Score> example = Example.of(score);
        List<Assemble_Choose_Score> scores = this.assembleChooseScoreRepository.findAll(example);
        for(Assemble_Choose_Score score1:scores){
            this.assembleChooseScoreRepository.deleteById(score1.getTcid());
        }
    }

    public Instruction getInstructionByinstrid(Long instrid){
        try{
            return this.instructionRepository.findById(instrid).get();
        } catch (Exception e){
            return null;
        }
    }


    public List<Task> getTaskbyQid(Long qid){
        Question_Task question_task = new Question_Task();
        question_task.setQid(qid);
        Example<Question_Task> example = Example.of(question_task);
        List<Question_Task> question_tasks = this.taskQuestionRepository.findAll(example);
        List<Task> tasks = new LinkedList<>();
        for(Question_Task questionTask : question_tasks){
            try{
                Task tempT = this.taskRepository.findById(questionTask.getTid()).get();
                if(tempT != null) tasks.add(tempT);
            } catch (EntityNotFoundException e){
                System.out.println("不存在该题号");
                taskQuestionRepository.deleteById(questionTask.getQtid());
                continue;
            }
        }
        return tasks;
    }


    public List<Assemble_Choose> getAssebleChoosesByTid(Long tid){
        Assemble_Choose assemble_choose = new Assemble_Choose();
        assemble_choose.setTid(tid);
        Example<Assemble_Choose> exampleAssemble = Example.of(assemble_choose);
        try {
            return this.assembleChooseRepository.findAll(exampleAssemble);
        } catch (Exception e){
            return new LinkedList<>();
        }
    }

    public List<Assemble_Choose> getAssembleChooseByTidAndPartId(Long tid,int partId){
        Assemble_Choose assemble_choose = new Assemble_Choose();
        assemble_choose.setTid(tid);
        assemble_choose.setTpart(partId);
        Example<Assemble_Choose> exampleAssemble = Example.of(assemble_choose);
        try {
            return this.assembleChooseRepository.findAll(exampleAssemble);
        } catch (Exception e){
            return new LinkedList<>();
        }
    }

//    根据题目id删除题目，并且删除所有学生该题目记录
    public void deletTaskByTid(Long tid){
        Task task = this.taskRepository.findById(tid).get();
//        如果是汇编题的话，删除所有选择题
        if (task.getTtype() == 1L){
            try {
                Assemble_Choose assemble_choose = new Assemble_Choose();
                assemble_choose.setTid(tid);
                Example<Assemble_Choose> exampleAssemble = Example.of(assemble_choose);
                List<Assemble_Choose> assemble_chooses = this.assembleChooseRepository.findAll(exampleAssemble);
                for(Assemble_Choose assemble_choose1 : assemble_chooses){
                    this.assembleChooseRepository.delete(assemble_choose1);
                }
            } catch (Exception e){
                System.out.println("删除选择题失败  " + tid + "  " + e.toString());
            }
            try{
                if(task.getSimuPicPath1() != Simulation.EXAMPLE_SIMULATION_PICPATH){
                    System.out.println(task.getSimuPicPath1());
                    FileUtil.deleteDirectory(task.getSimuPicPath1());
                }
                if(task.getSimuPicPath2() != Simulation.EXAMPLE_SIMULATION_PICPATH){
                    FileUtil.deleteDirectory(task.getSimuPicPath2());
                }
            }catch (Exception e){
                System.out.println(e.toString());
            }
        }

        FileUtil.deleteFileByTid(tid);
//        删除git
        try {
            GitProcess gitProcess = new GitProcess();
            gitProcess.deleteGroupByTid(tid);
        } catch (Exception e){
//            System.out.println(e.toString());
        }
        this.taskRepository.deleteById(tid);

//        删除跟这道题有关的所有分数
        try {
            Score score = new Score();
            score.setTid(tid);
            Example<Score> example = Example.of(score);
            List<Score> scores = this.scoreRepository.findAll(example);
            for(Score score1:scores){
                this.scoreRepository.deleteById(score1.getSid());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
//        删除所有作业里的这个题
        try {
            Question_Task question_task = new Question_Task();
            question_task.setTid(tid);
            Example<Question_Task> exampleQuesTask = Example.of(question_task);
            List<Question_Task> question_tasks = this.taskQuestionRepository.findAll(exampleQuesTask);
            for(Question_Task question_task1:question_tasks){
                this.taskQuestionRepository.deleteById(question_task1.getQtid());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveTemporaryData(Stage stage){
        this.stageRepository.save(stage);
    }

    /**
     * 查询学生的暂存信息
     * @param uid 用户id
     * @param tid 题目id
     * @return 暂存信息
     */
    public Stage findTemporaryData(Long uid,Long tid){
        try {
            return this.stageRepository.findByUidAndTid(uid,tid);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取对应课程下的所有题目信息
     * @param courseId  课程id
     * @return 题目列表
     */
    public List<Task> getTasksByCourseId(Long courseId){
        Task task=new Task();
        task.setCourseId(courseId);
        Example<Task> taskExample=Example.of(task);
        try{
            return this.taskRepository.findAll(taskExample);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 将对应的题目公开到公共题库
     * @param tid 题目id
     * @return 操作是否成功
     */
    public boolean makeTaskPublic(Long tid){
        try {
            Task task=this.taskRepository.findById(tid).get();
            task.setIsPublic(1);
            this.taskRepository.save(task);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 取消公开
     * @param tid 题目id
     * @return 操作是否成功
     */
    public boolean cancelTaskPublic(Long tid){
        try {
            Task task=this.taskRepository.findById(tid).get();
            task.setIsPublic(0);
            this.taskRepository.save(task);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取题库中的公开题目
     * @return 题目信息的列表
     */
    public List<Task> getPublicTasks(){
        Task task=new Task();
        task.setIsPublic(1);
        Example<Task> taskExample=Example.of(task);
        try {
            return this.taskRepository.findAll(taskExample);
        }catch (Exception e){
            return null;
        }
    }


    /**
     * 通过innerId查看对用的汇编仿真器
     * @param innerId innerid
     * @return 仿真器信息
     */
    public Simulation getSimulationByInnerId(Long innerId){
        Simulation simulation=new Simulation();
        simulation.setInnerid(innerId);
        Example<Simulation> simulationExample=Example.of(simulation);
        try {
            return this.simulationRepository.findOne(simulationExample).get();
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 通过指令集名称进行查找
     * @param instrName 指令集名称
     * @return 指令集信息
     */
    public Instruction getInstructionByName(String instrName){
        Instruction instruction=new Instruction();
        instruction.setInstrname(instrName);
        Example<Instruction> instructionExample=Example.of(instruction);
        try {
            return this.instructionRepository.findOne(instructionExample).get();
        }catch (Exception e){
            return null;
        }
    }

    public List<Task> findAllTask(){
        return this.taskRepository.findAll();
    }
}
