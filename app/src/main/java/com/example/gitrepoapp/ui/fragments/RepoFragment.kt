package com.example.gitrepoapp.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.gitrepoapp.R
import com.example.gitrepoapp.databinding.FragmentRepoBinding
import com.example.gitrepoapp.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_repo.*

@AndroidEntryPoint
class RepoFragment : Fragment(R.layout.fragment_repo) {

    private var _binding: FragmentRepoBinding? = null
    private val binding get() = _binding!!
    private val args: RepoFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        _binding = FragmentRepoBinding.bind(view)

        binding.apply {
            name.text = args.repo.name
            username.text = args.repo.owner.login
            language.text = args.repo.language
            description.text = args.repo.description

            avatar.apply {
                Glide.with(view)
                    .load(args.repo.owner.avatar_url)
                    .error(android.R.drawable.stat_notify_error)
                    .into(this)
            }

            stars.text = args.repo.stars.toString()
            forks.text = args.repo.forks.toString()
            watchers.text = args.repo.watchers.toString()
            issuesOpened.text = args.repo.openIssues.toString()
            createDate.text = DateUtils.formatDate(args.repo.createDate)
            updateDate.text = DateUtils.formatDate(args.repo.updateDate)
            btnBrowse.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(args.repo.url))
                startActivity(browserIntent)
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                view?.let { Navigation.findNavController(it).navigateUp() }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}