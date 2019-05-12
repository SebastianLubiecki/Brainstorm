package pl.brainstorm.question.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.brainstorm.question.Controllers.AuthorController;
import pl.brainstorm.question.Domain.Entities.AnswerEntity;
import pl.brainstorm.question.Domain.Repositories.AnswerRepository;
import pl.brainstorm.question.Models.Answer;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerService {

    private final static Logger logger = LoggerFactory.getLogger(AuthorController.class);
    private final AnswerRepository answerRepository;
    private final MappingService mappingService;

    @Autowired
    public AnswerService(AnswerRepository answerRepository, MappingService mappingService) {
        this.answerRepository = answerRepository;
        this.mappingService = mappingService;
    }

    public List<Answer> responseListForGivenQuestion (Long questionId){
        List<AnswerEntity> answerEntityList = answerRepository.findAllResponsesByQuestionId(questionId);
        List<Answer> answerList = new ArrayList<>();
        for(AnswerEntity answerEntity : answerEntityList){
            answerList.add(mappingService.map(answerEntity));
        }
        return answerList;
    }

    public String getCorrectAnswersForGivenQuestion(Long questionId){
        List<AnswerEntity> answerEntityList = answerRepository.findAllResponsesByQuestionId(questionId);
        answerEntityList.stream().filter(a->a.equals(true)).findFirst(); //byc może zrobić to w Questions
        AnswerEntity answerEntity = answerRepository.findCorrectResponseByQuestionId(questionId);
        if(answerEntity.getACorrect()){
            return answerEntity.getAnswerA();
        } else if(answerEntity.getBCorrect()){
            return answerEntity.getAnswerB();
        } else if (answerEntity.getCCorrect()){
            return answerEntity.getAnswerC();
        } else {
            return answerEntity.getAnswerD();
        }
    }

    public Long addAnswer(Answer answer) {
        return mappingService.map(answer).getId();
    }

    public Boolean removeAnswer(Long id){
        boolean isDeleted = false;
        if (answerRepository.existsById(id)) {
            answerRepository.deleteById(id);
            isDeleted = true;
        }
        return isDeleted;
    }

    public Answer editAnswer(Answer answer, Long id){
        if (answerRepository.existsById(id)) {
            AnswerEntity answerEntity = answerRepository.getOne(id);
            answerEntity.setAnswerA(answer.getAnswerA());
            answerEntity.setACorrect(answer.getACorrect());
            answerEntity.setAnswerB(answer.getAnswerB());
            answerEntity.setBCorrect(answer.getBCorrect());
            answerEntity.setAnswerC(answer.getAnswerC());
            answerEntity.setCCorrect(answer.getCCorrect()); //do przepatrzenia...
            answerEntity.setAnswerD(answer.getAnswerD());
            answerEntity.setDCorrect(answer.getDCorrect());
            answerRepository.save(answerEntity);
            return mappingService.map(answerEntity);
        }
        return null;
    }











}