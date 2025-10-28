package com.example.loyaltyprogram.controller;

import com.example.loyaltyprogram.model.Benefit;
import com.example.loyaltyprogram.model.PointHistory;
import com.example.loyaltyprogram.model.Stage;
import com.example.loyaltyprogram.service.LoyaltyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LoyaltyController
 * ロイヤルティプログラムの機能を管理するためのコントローラークラス。
 */
@Controller
@RequestMapping("/loyalty")
public class LoyaltyController {

    @Autowired
    private LoyaltyService loyaltyService;

    /**
     * ポイント履歴を表示するエンドポイント。
     * GET /loyalty/points/history
     *
     * @param userId ユーザーID（クエリパラメータ）
     * @param model  HTMLテンプレートにデータを渡すためのModelオブジェクト
     * @return ポイント履歴画面 (PointHistory.html)
     */
    @GetMapping("/points/history")
    public String getPointsHistory(@RequestParam("userId") String userId, Model model) {
        List<PointHistory> pointHistoryList = loyaltyService.getPointHistory(userId);
        model.addAttribute("pointHistoryList", pointHistoryList);
        return "PointHistory";
    }

    /**
     * ポイントを利用する情報を表示するエンドポイント。
     * GET /loyalty/points/use
     *
     * @param userId ユーザーID（クエリパラメータ）
     * @param model  HTMLテンプレートにデータを渡すためのModelオブジェクト
     * @return ポイント利用画面 (PointUsage.html)
     */
    @GetMapping("/points/use")
    public String getPointUsage(@RequestParam("userId") String userId, Model model) {
        int availablePoints = loyaltyService.getAvailablePoints(userId);
        model.addAttribute("availablePoints", availablePoints);
        return "PointUsage"; // Returns HTML view for point usage
    }

    /**
     * ポイントを利用して割引を実行するエンドポイント。
     * POST /loyalty/points/use
     *
     * @param userId ユーザーID（クエリパラメータ）
     * @param points 使用するポイント数（クエリパラメータ）
     * @return 処理結果の文字列（成功/失敗）
     */
    @PostMapping("/points/use")
    @ResponseBody
    public String usePoints(@RequestParam("userId") String userId, @RequestParam("points") int points) {
        boolean isUsed = loyaltyService.usePoints(userId, points);
        if (isUsed) {
            return "ポイントが正常に使用されました。";
        } else {
            return "ポイントの使用に失敗しました。ポイントが不足しています。";
        }
    }

    /**
     * 現在のステージをチェックするエンドポイント。
     * GET /loyalty/stage/check
     *
     * @param userId ユーザーID（クエリパラメータ）
     * @param model  HTMLテンプレートにデータを渡すためのModelオブジェクト
     * @return ステージ確認画面 (StageCheck.html)
     */
    @GetMapping("/stage/check")
    public String checkStage(@RequestParam("userId") String userId, Model model) {
        Stage stage = loyaltyService.getStageDetails(userId);
        model.addAttribute("stage", stage);
        return "StageCheck";
    }

    /**
     * ステージを更新（アップまたはダウン）するエンドポイント。
     * GET /loyalty/stage/update
     *
     * @param userId ユーザーID（クエリパラメータ）
     * @return 処理結果の文字列（成功/失敗）
     */
    @GetMapping("/stage/update")
    @ResponseBody
    public String updateStage(@RequestParam("userId") String userId) {
        String result = loyaltyService.updateStage(userId);
        return result;
    }

    /**
     * ロイヤルティメンバーの特典を表示するエンドポイント。
     * GET /loyalty/benefits
     *
     * @param model HTMLテンプレートにデータを渡すためのModelオブジェクト
     * @return 特典表示画面 (SpecialBenefits.html)
     */
    @GetMapping("/benefits")
    public String getBenefits(Model model) {
        List<Benefit> benefitsList = loyaltyService.getAvailableBenefits();
        model.addAttribute("benefitsList", benefitsList);
        return "SpecialBenefits";
    }

    /**
     * 特定の特典を適用するエンドポイント。
     * POST /loyalty/benefits/apply/{benefitId}
     *
     * @param benefitId 適用する特典ID（パスパラメータ）
     * @param userId    ユーザーID（クエリパラメータ）
     * @return 処理結果の文字列（成功/失敗）
     */
    @PostMapping("/benefits/apply/{benefitId}")
    @ResponseBody
    public String applyBenefit(@PathVariable("benefitId") Long benefitId, @RequestParam("userId") String userId) {
        boolean isApplied = loyaltyService.applyBenefit(userId, benefitId);
        if (isApplied) {
            return "特典を正常に適用しました。";
        } else {
            return "特典の適用に失敗しました。";
        }
    }

    /**
     * ポイントの有効期限を通知するエンドポイント。
     * POST /loyalty/notify
     *
     * @param userId ユーザーID（クエリパラメータ）
     * @return 処理結果の文字列（成功/失敗）
     */
    @PostMapping("/notify")
    @ResponseBody
    public String notifyExpiration(@RequestParam("userId") String userId) {
        boolean isNotified = loyaltyService.notifyExpiration(userId);
        if (isNotified) {
            return "有効期限通知を正常に送信しました。";
        } else {
            return "通知の送信に失敗しました。";
        }
    }
}